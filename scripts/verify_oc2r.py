#!/usr/bin/env python3
"""Pinned native implementation/data contract; no Minecraft, caches or archived-run fixtures."""
import argparse
import hashlib
import io
import json
import re
import subprocess
import zipfile

ARCHIVE = 'd6c42cffda0fa16734e14e26013320b4f1a05278544dd5c5b8d05914bfbb8632'
CLASSES = {
    'li/cil/oc2/common/Main$NativeLoader.class': 'b5f32cff1dbbc882d5de1482549d2824006b48386f6bf5d982d69a6deb6e6fb5',
    'li/cil/oc2/common/inet/DefaultSessionLayer.class': '6f4a8b9c92b79f26695b3f21525e3c6c7649f16bb05903c11c8ce0e6d74bf0b8',
    'li/cil/oc2/common/inet/DefaultSessionLayer$EchoResponse.class': 'fdbe872bfeb8c3c967ac8548f098fa577d11f5058f6770b639dae0ab17d1f9e0',
    'li/cil/oc2/common/bus/device/data/FileSystems.class': '621e1557cba40ad486aaa59cfc0b280910af69e088ef9c808f82b843f0eb2b12',
}
DATA = {
    'README.md': '9cc643438184ae144196fbd65268acfe1f1bcaf912aa3d3dd509760ae1b8c68c',
    'rootfs.json': '223d3da31c8595cf9a83053ef612266386ce99cd4be023fe0282474d676ebdbe',
    'rootfs.dsk': 'cf46784e33abfe678c5635a73deb6b3e8cf1b888492475ad8ba4eccc8902574b',
    'scripts.json': '118f89e3aa16256c7bc07c2dd7147f05a9aec8dbf6a7e8af3d657e78fcc756f7',
    'scripts.zip': '34066cda83ea2b274fb0b27d03391ae190b067d6d13cc12af7795b8802cbba85',
}
def sha(data): return hashlib.sha256(data).hexdigest()
def require(value, message):
    if not value: raise AssertionError(message)
def descriptor(data, resources):
    obj = json.loads(data)
    require(isinstance(obj, dict), 'Descriptor must be object')
    require(obj.get('type') in ('block', 'layer'), 'Unknown descriptor type')
    location = obj.get('location')
    require(isinstance(location, str) and re.fullmatch(r'[a-z0-9_.-]+:[a-z0-9/._-]+', location), 'Invalid location')
    require(location in resources, 'Missing referenced filesystem')
    return obj

def verify(path):
    with open(path, 'rb') as f: require(sha(f.read()) == ARCHIVE, 'Unreviewed OC2R archive')
    with zipfile.ZipFile(path) as z:
        for name, digest in CLASSES.items(): require(sha(z.read(name)) == digest, 'Unreviewed class ' + name)
        for name, digest in DATA.items(): require(sha(z.read('data/oc2r/file_systems/' + name)) == digest, 'Unreviewed resource ' + name)
        resources = {'oc2r:file_systems/' + n for n in DATA}
        names = sorted(n for n in z.namelist() if n.startswith('data/oc2r/file_systems/') and n.endswith('.json'))
        require(names == ['data/oc2r/file_systems/rootfs.json', 'data/oc2r/file_systems/scripts.json'], 'Descriptor population drift')
        for name in names: descriptor(z.read(name), resources)
        with zipfile.ZipFile(io.BytesIO(z.read('data/oc2r/file_systems/scripts.zip'))) as scripts:
            require(len([n for n in scripts.namelist() if not n.endswith('/')]) == 17, 'Layer population drift')
        require(not 'file_systems/README.md'.endswith('.json'), 'Documentation admitted as descriptor')
        # Independent negative controls: documentation/invalid JSON/unknown types/bad or missing
        # references cannot earn compatibility proof. These are reader-oracle controls, not native reloads.
        bad = [z.read('data/oc2r/file_systems/README.md'), b'{', b'[]', b'{}',
               b'{"type":"other","location":"oc2r:file_systems/rootfs.dsk"}',
               b'{"type":"block","location":"oc2r:FILE.dsk"}',
               b'{"type":"block","location":"oc2r:file_systems/missing.dsk"}']
        for data in bad:
            try: descriptor(data, resources)
            except (AssertionError, ValueError, TypeError): pass
            else: raise AssertionError('Malformed descriptor accepted')
        try: descriptor(z.read(names[0]), set())
        except AssertionError: pass
        else: raise AssertionError('Missing actual resource accepted')
    def bytecode(cls):
        return subprocess.run(['javap', '-classpath', path, '-c', '-p', cls], check=True,
                              text=True, capture_output=True, timeout=30).stdout
    session = bytecode('li.cil.oc2.common.inet.DefaultSessionLayer')
    send = session.split('public void sendSession(', 1)[1].split('private boolean processQueue', 1)[0]
    # The snapshot site has a single dominating caller consumption, then flag read, then native
    # call/async capture of local2. No rewinding or retained caller is permitted in the repair.
    tokens = ['ByteBuffer.get:([B)', 'Main.LoadedLibrary:Z', 'Method sendICMP:', 'aload_2', 'Executor.execute:']
    cursor = 0
    for token in tokens:
        found = send.find(token, cursor); require(found >= 0, 'Missing/changed causal site ' + token); cursor = found + len(token)
    require(send.count('Main.LoadedLibrary:Z') == 1, 'Ambiguous flag injection')
    require('InetAddress.isReachable:' in session, 'Missing actual JVM reachability fallback')
    response = bytecode('li.cil.oc2.common.inet.DefaultSessionLayer$EchoResponse')
    require('ByteBuffer.remaining:' in response and 'ByteBuffer.get:([B)' in response, 'Changed echo snapshot constructor')
    fs = bytecode('li.cil.oc2.common.bus.device.data.FileSystems')
    require('String .json' in fs and 'String.endsWith:' in fs, 'Native reader predicate drift')
    require('ZipStreamFileSystem' in fs and 'ResourceBlockDeviceData' in fs, 'Native descriptor loader drift')
    loader = bytecode('li.cil.oc2.common.Main$NativeLoader')
    require('Loaded native library successfully but ICMP still failed, falling back to JVM UDP implementation' in loader,
            'Native capability diagnostic drift')
    print('Pinned OC2R bytecode + descriptor/data + malformed/missing-resource negatives PASS; not native acceptance')

if __name__ == '__main__':
    parser = argparse.ArgumentParser(); parser.add_argument('--archive', required=True)
    verify(parser.parse_args().archive)
