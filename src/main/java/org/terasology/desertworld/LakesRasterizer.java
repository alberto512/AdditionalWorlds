package org.terasology.desertworld;

import org.terasology.math.ChunkMath;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizerPlugin;
import org.terasology.world.generation.facets.SeaLevelFacet;
import org.terasology.world.generation.facets.SurfaceHeightFacet;
import org.terasology.world.generator.plugin.RegisterPlugin;

@RegisterPlugin
public class LakesRasterizer implements WorldRasterizerPlugin {
    private Block water;
    private Block grass;

    @Override
    public void initialize() {
        water = CoreRegistry.get(BlockManager.class).getBlock("Core:Water");
        grass = CoreRegistry.get(BlockManager.class).getBlock("Core:Grass");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        SurfaceHeightFacet surfaceHeightFacet = chunkRegion.getFacet(SurfaceHeightFacet.class);
        SeaLevelFacet seaLevelFacet = chunkRegion.getFacet(SeaLevelFacet.class);
        int seaLevel = seaLevelFacet.getSeaLevel();
        Vector3i[] neighbors = new Vector3i[4];
        // Make the lake
        for (Vector3i position : chunkRegion.getRegion()) {
            float surfaceHeight = surfaceHeightFacet.getWorld(position.x, position.z);

            if (position.y < seaLevel - 9 && position.y > surfaceHeight) {
                chunk.setBlock(ChunkMath.calcBlockPos(position), water);

                neighbors[0] = position.addX(1);
                neighbors[1] = position.addX(-2);
                neighbors[2] = position.addX(1).addY(1);
                neighbors[3] = position.addY(-2);

                for (Vector3i block : neighbors) {
                    if (!chunk.getBlock(block).getURI().toString().equals("engine:air")) {
                        chunk.setBlock(ChunkMath.calcBlockPos(block), grass);
                    }
                }
            }
        }
    }
}
