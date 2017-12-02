
package org.terasology.lavalandgenerator;

import org.terasology.math.ChunkMath;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizer;

public class LogRasterizer implements WorldRasterizer {
    Block log;

    @Override
    public void initialize() {
        log = CoreRegistry.get(BlockManager.class).getBlock("AdditionalWorlds:PetrifiedTrunk");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        LogFacet LogFacet = chunkRegion.getFacet(LogFacet.class);
        
        for(Vector3i block : LogFacet.getWorldRegion()) if(LogFacet.getWorld(block)) {
            chunk.setBlock(ChunkMath.calcBlockPos(block.add(0,1,0)),log);
            chunk.setBlock(ChunkMath.calcBlockPos(block.add(0,1,0)),log);
            chunk.setBlock(ChunkMath.calcBlockPos(block.add(0,1,0)),log);
            chunk.setBlock(ChunkMath.calcBlockPos(block.add(0,1,0)),log);
        }
    }
}
