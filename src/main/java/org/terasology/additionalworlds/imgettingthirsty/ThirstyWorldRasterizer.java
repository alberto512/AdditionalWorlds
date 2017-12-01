/*
 * Copyright 2017 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.additionalworlds.imgettingthirsty;

import org.terasology.math.ChunkMath;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizer;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

public class ThirstyWorldRasterizer implements WorldRasterizer {
    private Block hardStone;
    private Block deadBush;
    private Block lava;

    @Override
    public void initialize() {
        hardStone = CoreRegistry.get(BlockManager.class).getBlock("Core:HardStone");
        deadBush = CoreRegistry.get(BlockManager.class).getBlock("Core:DeadBush");
        lava = CoreRegistry.get(BlockManager.class).getBlock("Core:Lava");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        SurfaceHeightFacet surfaceHeightFacet = chunkRegion.getFacet(SurfaceHeightFacet.class);
        for (Vector3i position : chunkRegion.getRegion()) {
            float surfaceHeight = surfaceHeightFacet.getWorld(position.x, position.z);
            if (position.y < surfaceHeight) {
                chunk.setBlock(ChunkMath.calcBlockPos(position), hardStone);
                java.util.Random rand = new java.util.Random();
                int random = rand.nextInt(10000)+1;
                if (random >= 10000) {
                    // make some lava fountains
                    int lavaHeight = rand.nextInt(10)+20;
                    int lavaWidth = lavaHeight % 5 + 1;
                    for (int i = 0; i < lavaHeight; i++) {
                        for (int j = 0; j < lavaWidth; j++) {
                            chunk.setBlock(ChunkMath.calcBlockPos(position.addX(1)), lava);
                            chunk.setBlock(ChunkMath.calcBlockPos(position.addZ(1)), lava);
                        }
                        position.addX(-lavaWidth).addZ(-lavaWidth);
                        position.addY(1);
                    }
                }
                else if (random >= 9900) {
                    chunk.setBlock(ChunkMath.calcBlockPos(position.addY(1)), deadBush);
                }
            }
        }
    }
}
