#!/usr/bin/env python3
"""Finite owning fixture namespace/source/classfile contract; never starts Minecraft."""
import argparse,json,re,subprocess
from pathlib import Path
ROOT=Path(__file__).resolve().parents[1]
PACKAGE='com.bettercontent.betteroc2rcreatecontrols.nativefixture.mixin'
NAME='EchoExecutorMixin'
NAMES=[NAME,'FilesystemDescriptorInventoryMixin']
def verify(classes=None, config=None, sources=None, resources=None):
    config=config if config is not None else json.loads((ROOT/'src/nativeFixture/resources/better_oc2r_create_controls.native_fixture.mixins.json').read_text())
    assert config['package']==PACKAGE and config['mixins']==NAMES and config['required'] is True and config['injectors']['defaultRequire']==1,'Required exact owning mixin configuration drift'
    if resources:
        processed=json.loads((Path(resources)/'better_oc2r_create_controls.native_fixture.mixins.json').read_text())
        assert processed==config,'Processed/runtime fixture configuration drift'
    assert not config.get('client') and not config.get('server') and 'refmap' not in config,'Unreviewed fixture mixins/refmap; owning target explicitly remap=false'
    if sources is None:sources={str(p):p.read_text()for p in (ROOT/'src/nativeFixture/java').rglob('*.java')}
    other_reserved=[json.loads(p.read_text())['package'] for p in (ROOT/'src/main/resources').glob('*.mixins.json')]
    found=[]
    for path,text in sources.items():
        match=re.search(r'\bpackage\s+([\w.]+)\s*;',text);assert match,path
        package=match[1]
        assert not any(package==prefix or package.startswith(prefix+'.') for prefix in other_reserved),'Native fixture entered another reserved Mixin package: '+path
        if package==PACKAGE or package.startswith(PACKAGE+'.'):
            assert package==PACKAGE and Path(path).name in {name+'.java' for name in NAMES} and '@Mixin(' in text,'Ordinary/unconfigured source in reserved package: '+path
            assert 'remap = false' in text and 'require = 1, expect = 1, allow = 1' in text,'Required native injector drift'
            found.append(path)
    assert len(found)==len(NAMES),'Missing/duplicate configured fixture Mixin'
    if classes:
        folder=Path(classes)/PACKAGE.replace('.','/');actual={str(p.relative_to(folder))for p in folder.rglob('*.class')}
        assert actual=={name+'.class' for name in NAMES},'Ordinary/nested/unconfigured class in reserved package: '+str(actual)
        for name, annotation, target in [(NAME,'org.spongepowered.asm.mixin.injection.Redirect(', 'li.cil.oc2.common.inet.DefaultSessionLayer'),
                ('FilesystemDescriptorInventoryMixin','com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation(', 'li/cil/oc2/common/bus/device/data/FileSystems')]:
            text=subprocess.check_output(['javap','-p','-v','-classpath',str(classes),PACKAGE+'.'+name],text=True)
            for token in ('org.spongepowered.asm.mixin.Mixin(',annotation,target,'remap=false','require=1','expect=1','allow=1'):
                assert token in text,'Compiled required annotation drift: '+token
    build=(ROOT/'build.gradle.kts').read_text();assert 'exclude("**/nativefixture/**", "better_oc2r_create_controls.native_fixture.mixins.json")' in build,'Shipping fixture leak guard removed'
if __name__=='__main__':
    p=argparse.ArgumentParser();p.add_argument('--classes');p.add_argument('--resources');a=p.parse_args();verify(a.classes,resources=a.resources)
    print('PASS exact required fixture Mixin/source/reserved descendant/compiled annotation/archive exclusion contract; native application pending')
