package com.ethylol.magical_meringue.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class PlatoniumFeature extends Feature<NoFeatureConfig> {

    public PlatoniumFeature(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {

        Type type = Type.values()[rand.nextInt(4)];
        int radius = rand.nextInt(32)+32;

        double p = (1+Math.sqrt(5))/2;

        double[] vertX, vertY, vertZ;

        //A face is just a triple of vertices, so identify each vertex with an integer
        int[][] faces;
        if (type == Type.TETRAHEDRON) {
            double a = radius/Math.sqrt(3);
            vertX = new double[] {a,  a, -a, -a};
            vertY = new double[] {a, -a,  a, -a};
            vertZ = new double[] {a, -a, -a,  a};

            faces = new int[][] {{0,3,1}, {0,1,2}, {1,3,2}, {0,2,3}};
        }
        else if (type == Type.CUBE) {
            double a = radius/Math.sqrt(3);
            vertX = new double[] {a,  a,  a,  a, -a, -a, -a, -a};
            vertY = new double[] {a,  a, -a, -a,  a,  a, -a, -a};
            vertZ = new double[] {a, -a,  a, -a,  a, -a,  a, -a};

            faces = new int[][] {{0,1,5,4}, {0,2,3,1}, {0,4,6,2}, {4,5,7,6}, {2,6,7,3}, {1,3,7,5}};
        }
        else if (type == Type.OCTAHEDRON) {
            double a = radius;
            vertX = new double[] {a, -a, 0,  0, 0,  0};
            vertY = new double[] {0,  0, a, -a, 0,  0};
            vertZ = new double[] {0,  0, 0,  0, a, -a};

            faces = new int[][] {{0,4,3}, {0,2,4}, {1,4,2}, {1,3,4}, {0,3,5}, {0,5,2}, {1,2,5}, {1,5,3}};
        }
        else if (type == Type.DODECAHEDRON) {
            double a = radius/Math.sqrt(3);
            vertX = new double[] {a,  a,  a,  a, -a, -a, -a, -a,     0,    0,   0,    0, a/p, a/p, -a/p, -a/p, a*p,  a*p,-a*p, -a*p};
            vertY = new double[] {a,  a, -a, -a,  a,  a, -a, -a,   a*p,  a*p,-a*p, -a*p,   0,   0,    0,    0, a/p, -a/p, a/p, -a/p};
            vertZ = new double[] {a, -a,  a, -a,  a, -a,  a, -a,   a/p, -a/p, a/p, -1/p, a*p,-a*p,  a*p, -a*p,   0,    0,   0,    0};

            faces = new int[][] {{14, 6, 10, 2, 12}, {12,0,8,4,14}, {12,2,17,16,18}, {14,4,18,19,6}, {0,16,1,9,8}, {4,8,9,5,18}, {6,19,7,11,10}, {2,10,11,3,17}, {17,3,13,1,16}, {18,5,15,7,19}, {7,15,13,3,11}, {9,1,13,15,5}};
        }
        else {
            //ICOSAHEDRON
            double a = radius/Math.sqrt(p+2);
            vertX = new double[] {  0,   0,    0,    0, a,      a,  -a,   -a, a*p, a*p, -a*p, -a*p};
            vertY = new double[] {a*p, a*p, -a*p, -a*p, 0,      0,   0,    0,   a,  -a,    a,   -a};
            vertZ = new double[] {  a,  -a,    a,   -a, a*p, -a*p, a*p, -a*p,   0,   0,    0,    0};

            faces = new int[][] {{4,0,6}, {6,2,4}, {4,8,0}, {0,10,6}, {6,10,11}, {6,11,2}, {4,2,9}, {4,9,8}, {0,8,1}, {0,1,10}, {2,11,3}, {2,3,9}, {9,5,8}, {10,7,11}, {9,3,5}, {8,5,1}, {10,1,7}, {11,7,9}, {3,7,5}, {1,5,7}};
        }


        //rotate around X axis

        double theta = rand.nextDouble();
        for (int i = 0; i < vertX.length; i++) {
            double c = Math.cos(theta);
            double s = Math.sin(theta);
            double vY = vertY[i];
            double vZ = vertZ[i];

            vertY[i] = c*vY - s*vZ;
            vertZ[i] = s*vY + c*vZ;
        }

        //Y

        theta = rand.nextDouble();
        for (int i = 0; i < vertX.length; i++) {
            double c = Math.cos(theta);
            double s = Math.sin(theta);
            double vX = vertX[i];
            double vZ = vertZ[i];

            vertX[i] = c*vX + s*vZ;
            vertZ[i] = -s*vX + c*vZ;
        }

        //Z

        theta = rand.nextDouble();
        for (int i = 0; i < vertX.length; i++) {
            double c = Math.cos(theta);
            double s = Math.sin(theta);
            double vX = vertX[i];
            double vY = vertY[i];

            vertX[i] = c*vX - s*vY;
            vertY[i] = s*vX + c*vY;
        }


        for (int i = -radius; i <= radius; i++) {
            int jRange = (int) Math.sqrt(radius*radius - i*i);
            for (int j = -jRange; j <= jRange; j++) {
                int kRange = (int) Math.sqrt(radius*radius - i*i - j*j);
                for (int k = -kRange; k <= kRange; k++) {

                    Vector3d point = new Vector3d(i, j, k);

                    boolean inFeature = true;

                    for (int[] face : faces) {
                        // Get normal vector
                        // p0 = vertX[face[0]], vertY[face[0]], vertZ[face[0]]
                        // p1 = vertX[face[1]], vertY[face[1]], vertZ[face[1]]
                        // p2 = vertX[face[2]], vertY[face[2]], vertZ[face[2]]
                        // u = p1 - p0, v = p2 - p0
                        // normal vector = u cross v

                        Vector3d p0 = new Vector3d(vertX[face[0]], vertY[face[0]], vertZ[face[0]]);
                        Vector3d p1 = new Vector3d(vertX[face[1]], vertY[face[1]], vertZ[face[1]]);
                        Vector3d p2 = new Vector3d(vertX[face[2]], vertY[face[2]], vertZ[face[2]]);

                        Vector3d u = p1.subtract(p0);
                        Vector3d v = p2.subtract(p0);
                        Vector3d normal = u.crossProduct(v).normalize();

                        Vector3d pointToFace = p0.subtract(point);

                        double d = pointToFace.dotProduct(normal);
                        d /= pointToFace.length();

                        if (d < -1e-15) {
                            //not in feature
                            inFeature = false;
                        }
                    }

                    if (inFeature) {
                        this.setBlockState(reader, pos.add(i, j, k), Blocks.OBSIDIAN.getDefaultState());
                    }

                }
            }
        }


        return true;
    }

    public enum Type {
        TETRAHEDRON,
        CUBE,
        OCTAHEDRON,
        DODECAHEDRON,
        ICOSAHEDRON
    }

}
