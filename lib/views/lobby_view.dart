import 'package:flutter/material.dart';
import '../controllers/vr_experience_controller.dart';

class LobbyView extends StatelessWidget {
  final VrExperienceController controller;

  const LobbyView({super.key, required this.controller});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('OneXR Lobby')),
      body: Center(
        child: ElevatedButton(
          onPressed: controller.startExperience,
          child: const Text('Start Experience'),
        ),
      ),
    );
  }
}
