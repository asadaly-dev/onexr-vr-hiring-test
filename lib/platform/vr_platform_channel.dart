import 'package:flutter/services.dart';

class VrPlatformChannel {
  static const MethodChannel _channel =
  MethodChannel('com.onexr.vr/player');

  static Future<void> startVrExperience() async {
    await _channel.invokeMethod('startVrExperience');
  }
}