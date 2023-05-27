package util;

import math.Vec3;
import math.Vec4;
import mesh.Mesh;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class STLLoader {

    public Mesh load(String path) {
        List<String> lines = getLines(path);
        List<Vec3> vertices = new ArrayList<>();
        List<Vec3> colors = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        for(int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if(line.contains("vertex")) {
                String[] coords = line.split("\\s+");
                vertices.add(new Vec3(Double.parseDouble(coords[1]),Double.parseDouble(coords[2]),Double.parseDouble(coords[3])));
                indices.add(vertices.size()-1);
                colors.add(new Vec3(0,1,0));
            }
        }
        int[] index = new int[indices.size()];
        for(int i = 0; i < index.length;i++) index[i] = indices.get(i);
        return new Mesh(index, vertices.toArray(new Vec3[0]), colors.toArray(new Vec3[0]));
    }

    private List<String> getLines(String path) {
        try {
            return Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
