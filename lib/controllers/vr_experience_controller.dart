import 'package:flutter/material.dart';
import '../models/vr_experience_model.dart';
import '../platform/vr_platform_channel.dart';

class VrExperienceController extends ChangeNotifier {
  VrStatus _status = VrStatus.idle;
  VrStatus get status => _status;

  void startExperience() async {
    _status = VrStatus.starting;
    notifyListeners();

    try {
      await VrPlatformChannel.startVrExperience();
      markCompleted();
    }
    catch (e) {
      debugPrint("Error launching VR: $e");
      _status = VrStatus.idle;
      notifyListeners();
    }
  }

  void markCompleted() {
    _status = VrStatus.completed;
    notifyListeners();
  }
}
