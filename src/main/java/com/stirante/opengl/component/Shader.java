package com.stirante.opengl.component;

import com.stirante.opengl.util.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
//    public static final int VERTEX_ATTRIB = 0;
//    public static final int TCOORD_ATTRIB = 1;

    private int id;
    private Map<String, Integer> locationCache = new HashMap<>();
    private int vertID;
    private int fragID;

    public Shader(String path) {
        load(path);
    }

    private static String loadAsString(String file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String buffer;
            while ((buffer = reader.readLine()) != null) {
                result.append(buffer).append('\n');
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public void load(String path) {
        String vert = loadAsString(path + ".vert");
        String frag = loadAsString(path + ".frag");

        int program = glCreateProgram();
        vertID = glCreateShader(GL_VERTEX_SHADER);
        fragID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(vertID, vert);
        glShaderSource(fragID, frag);

        glCompileShader(vertID);
        if (glGetShaderi(vertID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile vertex shader!");
            System.err.println(glGetShaderInfoLog(vertID));
            System.exit(0);
            return;
        }

        glCompileShader(fragID);
        if (glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile fragment shader!");
            System.err.println(glGetShaderInfoLog(fragID));
            System.exit(0);
            return;
        }

        glAttachShader(program, vertID);
        glAttachShader(program, fragID);
        glLinkProgram(program);
        glValidateProgram(program);

        id = program;
    }

    public int getID() {
        return id;
    }

    public void enable() {
        glUseProgram(id);
    }

    public void disable() {
        glUseProgram(0);
    }

    public int getUniform(String name) {
        if (locationCache.containsKey(name))
            return locationCache.get(name);

        int result = glGetUniformLocation(id, name);
        if (result == -1)
            System.err.println("Could not find uniform variable '" + name + "'!");
        else
            locationCache.put(name, result);
        return result;
    }

    public void setUniform1i(String name, int value) {
        glUniform1i(getUniform(name), value);
    }

    public void setUniform1f(String name, float value) {
        glUniform1f(getUniform(name), value);
    }

    public void setUniform2f(String name, float x, float y) {
        glUniform2f(getUniform(name), x, y);
    }

    public void setUniform3f(String name, Vector3f vector) {
        glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
    }

    public void destroyGL() {
        glDeleteProgram(id);
        glDeleteShader(vertID);
        glDeleteShader(fragID);
    }
}
