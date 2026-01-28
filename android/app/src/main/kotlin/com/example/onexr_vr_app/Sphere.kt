package com.example.onexr_vr_app

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import kotlin.math.*

class Sphere(
    radius: Float = 50f,
    stacks: Int = 40,
    slices: Int = 40
) {

    val vertexBuffer: FloatBuffer
    val texBuffer: FloatBuffer
    val vertexCount: Int

    init {
        val vertices = ArrayList<Float>()
        val texCoords = ArrayList<Float>()

        for (i in 0 until stacks) {
            val phi1 = Math.PI * i / stacks
            val phi2 = Math.PI * (i + 1) / stacks

            for (j in 0 until slices) {
                val theta1 = 2 * Math.PI * j / slices
                val theta2 = 2 * Math.PI * (j + 1) / slices

                add(vertices, texCoords, radius, phi1, theta1)
                add(vertices, texCoords, radius, phi2, theta1)
                add(vertices, texCoords, radius, phi2, theta2)

                add(vertices, texCoords, radius, phi1, theta1)
                add(vertices, texCoords, radius, phi2, theta2)
                add(vertices, texCoords, radius, phi1, theta2)
            }
        }

        vertexCount = vertices.size / 3

        vertexBuffer = ByteBuffer.allocateDirect(vertices.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                vertices.forEach { put(it) }
                position(0)
            }

        texBuffer = ByteBuffer.allocateDirect(texCoords.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                texCoords.forEach { put(it) }
                position(0)
            }
    }

    private fun add(
        v: MutableList<Float>,
        t: MutableList<Float>,
        r: Float,
        phi: Double,
        theta: Double
    ) {
        val x = (r * sin(phi) * cos(theta)).toFloat()
        val y = (r * cos(phi)).toFloat()
        val z = (r * sin(phi) * sin(theta)).toFloat()

        v.add(-x)
        v.add(y)
        v.add(z)

        val u = (theta / (2 * Math.PI)).toFloat()
        val vTex = (phi / Math.PI).toFloat()

        t.add(1f - u)
        t.add(vTex)

    }
}
