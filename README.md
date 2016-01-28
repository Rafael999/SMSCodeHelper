Captchas Tool
===============

> 本项目为 [RikkaW/SmsCodeHelper](https://github.com/RikkaW/SmsCodeHelper) 的二次开发的完全不同画风版,主要变化为高度自定义的更多配置项,第三方 `sdk` 的使用

![](./app/src/main/res/mipmap-xxxhdpi/ic_launcher.png)

验证码处理工具,检查短信中的验证码并给予在通知栏和Toast显示,输出至剪贴板等操作


## Screener

![](./Screener/20151225191021.png)

## 更新记录(ChangeLog)
### [v0.0.6](./apk/me.gitai.smscodehelper-release-c7-v0.0.6.apk)
* 修复通知图标
* 增加测试功能
* 增加部分解析解析
* 去除Google验证码`G-`

### [v0.0.5](./apk/me.gitai.smscodehelper-release-c5-v0.0.5.apk)
* 优化整合解析器
* 更少配置项
	- 去除自定义触发关键词
	- 去除自定义来源及解析正则
* 修复透明状态栏及阴影重叠问题
* 增加测试用例 100+

### [v0.0.4](./apk/me.gitai.smscodehelper-release-c4-v0.0.4.apk)
* 增加简述(即说明)
* 增加配置项
	- 自定义触发关键词
	- 自定义来源及解析正则
* 反推服务商
* 拨号盘暗码: `*#*#767#*#*`  (SMS)
* 增加原生5.0以上Overview Screen卡片颜色

### [v0.0.3](./apk/me.gitai.smscodehelper-release-c3-v0.0.3.apk)
* 增加配置项
	- 启用/暂停服务
	- 自动复制到剪贴板
		* 检测剪贴板是否为空
	- 更科学的显示通知
		* 点击显示短信内容
		* 点击内容自动复制
		* 视图中显示来源/校验码/服务商等内容
* 新通知栏图标

### [v0.0.2(2)](./apk/me.gitai.smscodehelper-release-c2-v0.0.2-t12261829.apk)

* 新图标 @萌萌的小雅酱

### [v0.0.1(1)](./apk/me.gitai.smscodehelper-release-c1-v0.0.1-t12251854.apk)

* 增加 `魔趣 OS` 验证码解析 [`SDK`](http://opengrok.mokeedev.com/mkl-mr1/xref/external/mokee/MoKeeSDKs/libMoKeeCloud/libMoKeeCloud.jar)

### [v1.0.6(10006)](./apk/rikka.smscodehelper-release-c10006-v1.0.6-t12131130.apk)

* 重写/模块化
* 增加配置界面
* 增加正则表达式解析
* 增加通知提醒
* 精简兼容包(52.2kb)

### [v1.0.5(10005)](https://github.com/RikkaW/SmsCodeHelper)

* 支持含有大写字母的验证码
* 减少了一点点体积
* 新的图标

By:  [RikkaW](https://github.com/RikkaW)

## Images

- 萌萌的小雅酱
	+ mipmap-hdpi/ic_launcher.png
	+ mipmap-mdpi/ic_launcher.png
	+ mipmap-xhdpi/ic_launcher.png
	+ mipmap-xxhdpi/ic_launcher.png
	+ mipmap-xxxhdpi/ic_launcher.png
	+ drawable-hdpi/ic_notify.png
	+ drawable-mdpi/ic_notify.png
	+ drawable-xhdpi/ic_notify.png
	+ drawable-xxhdpi/ic_notify.png
	+ drawable-xxxhdpi/ic_notify.png

## Build with Gradle

1. `git clone https://github.com/RikkaW/SmsCodeHelper`
2.  create a key store file and change configuration
3. `cd SmsCodeHelper`
4. `gradle clean build`
5. `find -name "rikka.smscodehelper*.apk"`

## Signing Configs
```
releaseSigning : [
        storeFile : System.getenv("KEYSTORE"),
        storePassword : System.getenv("KEYSTORE_PASSWORD"),
        alias : System.getenv("KEY_ALIAS"),
        aliasPassword: System.getenv("KEY_PASSWORD")
],
```

## Licenses

GPLv3+

## Libraries

- [systembartint](https://github.com/jgilfelt/SystemBarTint)
