clean:
	gradle clean

debug:
	./build.sh

install:
	adb uninstall me.gitai.smscodehelper.debug
	adb install app/build/outputs/apk/me.gitai.smscodehelper-debug-c12-v0.1.0.apk