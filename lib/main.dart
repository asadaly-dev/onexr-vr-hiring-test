import 'package:flutter/material.dart';
import 'controllers/vr_experience_controller.dart';
import 'models/vr_experience_model.dart';
import 'views/lobby_view.dart';
import 'views/end_view.dart';

void main() {
  runApp(const OneXRApp());
}

class OneXRApp extends StatefulWidget {
  const OneXRApp({super.key});

  @override
  State<OneXRApp> createState() => _OneXRAppState();
}

class _OneXRAppState extends State<OneXRApp> {
  final VrExperienceController controller = VrExperienceController();

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: AnimatedBuilder(
        animation: controller,
        builder: (_, __) {
          if (controller.status == VrStatus.completed) {
            return const EndView();
          }
          return LobbyView(controller: controller);
        },
      ),
    );
  }
}
