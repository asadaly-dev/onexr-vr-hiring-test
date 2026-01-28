import 'package:flutter/material.dart';

class EndView extends StatelessWidget {
  const EndView({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Experience Ended')),
      body: const Center(
        child: Text(
          'Thank you for watching',
          style: TextStyle(fontSize: 18),
        ),
      ),
    );
  }
}
