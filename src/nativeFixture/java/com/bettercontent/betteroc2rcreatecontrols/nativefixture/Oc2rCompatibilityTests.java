package com.bettercontent.betteroc2rcreatecontrols.nativefixture;

import com.google.gson.Gson;
import li.cil.oc2.api.inet.*;
import li.cil.oc2.api.inet.session.*;
import li.cil.oc2.common.Main;
import li.cil.oc2.common.bus.device.data.FileSystems;
import li.cil.oc2.common.inet.DefaultSessionLayer;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.gametest.GameTestHolder;
import org.apache.logging.log4j.LogManager;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.file.*;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;
import java.util.zip.ZipInputStream;

@GameTestHolder("better_oc2r_create_controls")
@net.minecraftforge.gametest.PrefixGameTestTemplate(false)
public final class Oc2rCompatibilityTests {
    private static final String ARCHIVE = "d6c42cffda0fa16734e14e26013320b4f1a05278544dd5c5b8d05914bfbb8632";
    private static final String TARGET = "6f4a8b9c92b79f26695b3f21525e3c6c7649f16bb05903c11c8ce0e6d74bf0b8";
    private static final String ROOTFS = "cf46784e33abfe678c5635a73deb6b3e8cf1b888492475ad8ba4eccc8902574b";
    private static final String SCRIPTS = "34066cda83ea2b274fb0b27d03391ae190b067d6d13cc12af7795b8802cbba85";
    private static boolean network, filesystems, hold;
    private static final List<Runnable> held = new ArrayList<>();

    public static void dispatch(Executor executor, Runnable task) {
        if (hold) held.add(() -> executor.execute(task));
        else executor.execute(task);
    }

    private static void require(boolean value, String message) {
        if (!value) throw new AssertionError(message);
    }
    private static String sha(byte[] bytes) throws Exception {
        return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
    }
    private static void await(BooleanSupplier ready) throws Exception {
        long deadline = System.nanoTime() + 3_000_000_000L;
        while (!ready.getAsBoolean()) {
            if (System.nanoTime() >= deadline) throw new AssertionError("Native networking deadline");
            Thread.sleep(2);
        }
    }
    private static void publish() throws Exception {
        if (!network || !filesystems) return;
        Path directory = Path.of(System.getProperty("user.dir")).toRealPath();
        Map<String,Object> proof = new LinkedHashMap<>();
        proof.put("schema", "bc.oc2r_compat_proof.v1");
        proof.put("fixture_directory", directory.toString());
        proof.put("archive_sha256", ARCHIVE);
        proof.put("target_class_sha256", TARGET);
        Path loaded = net.minecraftforge.fml.ModList.get().getModFileById("better_compat_fixes").getFile().getFilePath();
        Path provider = Path.of(Objects.requireNonNull(System.getenv("BC_CUSTOM_MOD_JAR_DIR")), "better-compat-fixes-0.1.9.jar");
        try (var actual = new java.util.zip.ZipFile(loaded.toFile()); var canonical = new java.util.zip.ZipFile(provider.toFile())) {
            // These MC-free repair classes/config and source identity must survive ForgeGradle remapping exactly.
            for (String name : List.of("com/bettercontent/bettercompatfixes/mixin/oc2r/EchoPayloadMixin.class",
                    "com/bettercontent/bettercompatfixes/compat/Oc2rEchoPayload.class",
                    "better_compat_fixes.oc2r.mixins.json", "META-INF/better-content-source.properties")) {
                try (InputStream a = actual.getInputStream(actual.getEntry(name));
                     InputStream b = canonical.getInputStream(canonical.getEntry(name))) {
                    require(Arrays.equals(a.readAllBytes(), b.readAllBytes()), "Loaded/canonical Compat identity differs: " + name);
                }
            }
        }
        proof.put("compat_provider_sha256", sha(Files.readAllBytes(provider)));
        proof.put("compat_loaded_provider_sha256", sha(Files.readAllBytes(loaded)));
        proof.put("network", Map.of("echo_snapshot", true, "caller_consumed", true,
                "udp_roundtrip", true, "tcp_roundtrip", true));
        proof.put("filesystems", Map.of("rootfs_sha256", ROOTFS, "scripts_zip_sha256", SCRIPTS,
                "all_descriptors_loaded", true));
        byte[] bytes = (new Gson().toJson(proof) + "\n").getBytes(java.nio.charset.StandardCharsets.UTF_8);
        Files.write(directory.resolve("oc2r-compat-proof.json"), bytes, StandardOpenOption.CREATE_NEW);
        LogManager.getLogger().info("BC_OC2R_COMPAT_V1 proof_sha256={}", sha(bytes));
    }

    @GameTest(template = "blank", timeoutTicks = 400)
    public static void nativeNetworkFallbackPreservesPayload(GameTestHelper helper) throws Exception {
        boolean original = Main.LoadedLibrary;
        List<Runnable> ticks = new ArrayList<>();
        InternetManager manager = task -> { ticks.add(task); return () -> ticks.remove(task); };
        DefaultSessionLayer layer = new DefaultSessionLayer(new LayerParameters() {
            public Optional<Tag> getSavedState() { return Optional.empty(); }
            public InternetManager getInternetManager() { return manager; }
        });
        Main.LoadedLibrary = false;
        try {
            byte[] expected = {31, 41, 59, 26, 53};
            // Different caller storage, positions and limits. Original tasks are held until
            // AFTER every caller has consumed/reused its storage, never replacing reachability.
            for (int kind = 0; kind < 4; kind++) {
                ByteBuffer storage = kind == 1 ? ByteBuffer.allocateDirect(16) : ByteBuffer.allocate(16);
                storage.position(3).put(expected).limit(8).position(3);
                ByteBuffer caller = kind == 2 ? storage.slice() : kind == 3 ? storage.asReadOnlyBuffer() : storage;
                int limit = caller.limit();
                int start = caller.position();
                caller.mark();
                Echo echo = new Echo();
                ByteBuffer reply = ByteBuffer.allocate(64);
                boolean[] received = {false};
                hold = true;
                layer.sendSession(echo, caller);
                require(caller.position() == limit && caller.limit() == limit, "Caller consumption/limit changed");
                require(caller.reset().position() == start, "Caller mark changed");
                caller.position(limit);
                require(held.size() == 1, "Expected exactly one original fallback task");
                storage.clear(); while (storage.hasRemaining()) storage.put((byte) 0x7f);
                hold = false;
                held.remove(0).run();
                await(() -> {
                    layer.receiveSession(s -> { require(s == echo, "Wrong echo session"); received[0] = true; return reply; });
                    return received[0];
                });
                byte[] actual = new byte[reply.remaining()]; reply.get(actual);
                require(Arrays.equals(expected, actual), "Fallback lost/corrupted immutable echo payload");
                received[0] = false;
                layer.receiveSession(s -> { received[0] = true; return ByteBuffer.allocate(64); });
                require(!received[0], "Echo callback repeated");
            }
            layer.sendSession(new Echo(), null);
            require(held.isEmpty(), "Null payload scheduled work");
            Echo empty = new Echo();
            ByteBuffer emptyReply = ByteBuffer.allocate(1);
            boolean[] emptyReceived = {false};
            layer.sendSession(empty, ByteBuffer.allocate(0));
            await(() -> {
                layer.receiveSession(s -> { require(s == empty, "Wrong empty echo session"); emptyReceived[0] = true; return emptyReply; });
                return emptyReceived[0];
            });
            require(emptyReply.remaining() == 0, "Empty echo invented data");
            // Real local sockets: no network permission bypass, mocked channels or synthetic replies.
            try (DatagramSocket peer = new DatagramSocket(new InetSocketAddress("127.0.0.1", 0))) {
                peer.setSoTimeout(3000);
                Datagram session = new Datagram(peer.getLocalPort());
                layer.sendSession(session, ByteBuffer.wrap(expected));
                session.state = Session.States.ESTABLISHED;
                DatagramPacket packet = new DatagramPacket(new byte[64], 64);
                peer.receive(packet);
                require(Arrays.equals(expected, Arrays.copyOf(packet.getData(), packet.getLength())), "UDP send mismatch");
                peer.send(packet);
                ByteBuffer reply = ByteBuffer.allocate(64);
                boolean[] received = {false};
                await(() -> {
                    for (Runnable tick : List.copyOf(ticks)) tick.run();
                    layer.receiveSession(s -> { require(s == session, "Wrong UDP session"); received[0] = true; return reply; });
                    return received[0];
                });
                byte[] actual = new byte[reply.remaining()]; reply.get(actual);
                require(Arrays.equals(expected, actual), "UDP response mismatch");
                session.state = Session.States.FINISH; layer.sendSession(session, null);
            }
            try (ServerSocket peer = new ServerSocket(0, 1, InetAddress.getByName("127.0.0.1"))) {
                peer.setSoTimeout(3000);
                Stream session = new Stream(peer.getLocalPort());
                layer.sendSession(session, null);
                try (Socket connection = peer.accept()) {
                    connection.setSoTimeout(3000);
                    await(() -> {
                        for (Runnable tick : List.copyOf(ticks)) tick.run();
                        layer.receiveSession(s -> session.received);
                        return session.state == Session.States.ESTABLISHED;
                    });
                    ByteBuffer outgoing = ByteBuffer.wrap(expected);
                    await(() -> { layer.sendSession(session, outgoing); return !outgoing.hasRemaining(); });
                    require(Arrays.equals(expected, connection.getInputStream().readNBytes(expected.length)), "TCP send mismatch");
                    connection.getOutputStream().write(expected);
                    await(() -> {
                        for (Runnable tick : List.copyOf(ticks)) tick.run();
                        layer.receiveSession(s -> session.received);
                        return session.received.position() == expected.length;
                    });
                    session.received.flip(); byte[] actual = new byte[session.received.remaining()]; session.received.get(actual);
                    require(Arrays.equals(expected, actual), "TCP response mismatch");
                    session.state = Session.States.FINISH; layer.sendSession(session, null);
                }
            }
            network = true; publish(); helper.succeed();
        } finally {
            hold = false;
            // Every captured task must have been forwarded before scope closure.
            for (Runnable task : List.copyOf(held)) task.run();
            held.clear(); layer.onStop(); Main.LoadedLibrary = original;
        }
    }

    @GameTest(template = "blank", timeoutTicks = 400)
    public static void nativeFilesystemDescriptorsLoadAllBytes(GameTestHelper helper) throws Exception {
        var manager = FileSystems._resourceManager;
        // Same entire effective preparse .json inventory returned to the native loader, not a second scan.
        var descriptors = FilesystemDescriptorInventory.requireSingleCapture();
        require(descriptors.equals(Set.of("oc2r:file_systems/rootfs.json",
                "oc2r:file_systems/scripts.json")), "Unexpected/missing filesystem descriptor population");
        for (String descriptor : descriptors) {
            String expected = descriptor.endsWith("rootfs.json")
                    ? "223d3da31c8595cf9a83053ef612266386ce99cd4be023fe0282474d676ebdbe"
                    : "118f89e3aa16256c7bc07c2dd7147f05a9aec8dbf6a7e8af3d657e78fcc756f7";
            try (InputStream input = manager.getResource(new ResourceLocation(descriptor)).orElseThrow().open()) {
                require(sha(input.readAllBytes()).equals(expected), "Native descriptor bytes mismatch");
            }
        }
        var block = FileSystems.getBlockData().get(new ResourceLocation("oc2r", "file_systems/rootfs.dsk"));
        require(block != null && block == FileSystems.getBlockByName("rootfs"), "Native rootfs not registered");
        require(block.getBlockDevice().getCapacity() == 8388608, "Native rootfs capacity mismatch");
        // Pinned Sedna's stream returns -1 for a zero-length bulk read. JDK17 readAllBytes
        // probes that case at an 8192-byte boundary; transferTo uses positive-size reads.
        try (InputStream input = block.getBlockDevice().getInputStream();
             ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {
            input.transferTo(bytes);
            require(sha(bytes.toByteArray()).equals(ROOTFS), "Native rootfs bytes mismatch");
        }
        byte[] scripts;
        try (InputStream input = manager.getResource(new ResourceLocation("oc2r", "file_systems/scripts.zip")).orElseThrow().open()) {
            scripts = input.readAllBytes();
        }
        require(sha(scripts).equals(SCRIPTS), "Scripts resource mismatch");
        int files = 0;
        try (ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(scripts))) {
            java.util.zip.ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                if (entry.isDirectory()) continue;
                byte[] expected = zip.readAllBytes();
                var path = new li.cil.sedna.fs.Path();
                for (String component : entry.getName().split("/")) path = path.resolve(component);
                var fs = FileSystems.getLayeredFileSystem();
                require(fs.exists(path), "Missing native scripts layer entry " + entry.getName());
                try (var handle = fs.open(path, li.cil.sedna.fs.FileMode.READ)) {
                    ByteBuffer bytes = ByteBuffer.allocate(expected.length + 1);
                    int count = handle.read(0, bytes);
                    require(count == expected.length && Arrays.equals(expected, Arrays.copyOf(bytes.array(), count)),
                            "Native scripts layer bytes mismatch " + entry.getName());
                }
                files++;
            }
        }
        require(files == 17, "Unreviewed scripts layer population");
        filesystems = true; publish(); helper.succeed();
    }

    private static class Base implements Session {
        final InetSocketAddress destination;
        Session.States state = Session.States.NEW;
        Object attachment;
        Base(int port) { destination = new InetSocketAddress("127.0.0.1", port); }
        public long getId() { return 123; }
        public void close() { state = Session.States.FINISH; }
        public Session.States getState() { return state; }
        public Object getAttachment() { return attachment; }
        public void setAttachment(Object value) { attachment = value; }
        public InetSocketAddress getDestination() { return destination; }
        public Instant getLastUpdateTime() { return Instant.now(); }
    }
    private static final class Echo extends Base implements EchoSession {
        Echo() { super(0); state = Session.States.ESTABLISHED; }
        public int getSequenceNumber() { return 1; }
        public int getTtl() { return 64; }
    }
    private static final class Datagram extends Base implements DatagramSession {
        Datagram(int port) { super(port); }
    }
    private static final class Stream extends Base implements StreamSession {
        final ByteBuffer received = ByteBuffer.allocate(64);
        Stream(int port) { super(port); }
        public ByteBuffer getSendBuffer() { return ByteBuffer.allocate(64); }
        public ByteBuffer getReceiveBuffer() { return received; }
        public void connect() { state = Session.States.ESTABLISHED; }
    }
}
