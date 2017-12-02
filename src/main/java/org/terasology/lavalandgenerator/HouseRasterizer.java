
package org.terasology.lavalandgenerator;

import org.terasology.math.ChunkMath;
import org.terasology.math.Region3i;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizer;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

public class HouseRasterizer implements WorldRasterizer {
    Block stone, log, ash, air;

    @Override
    public void initialize() {
        stone = CoreRegistry.get(BlockManager.class).getBlock("AdditionalWorlds:VolcanicRock");
        log = CoreRegistry.get(BlockManager.class).getBlock("AdditionalWorlds:PetrifiedTrunk");
        ash = CoreRegistry.get(BlockManager.class).getBlock("AdditionalWorlds:Ash");
        BlockManager blockManager = CoreRegistry.get(BlockManager.class);
        air = blockManager.getBlock(BlockManager.AIR_ID);
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        HouseFacet houseFacet = chunkRegion.getFacet(HouseFacet.class);
        SurfaceHeightFacet surfaceHeightFacet = chunkRegion.getFacet(SurfaceHeightFacet.class);
        
        for(Vector3i block : houseFacet.getWorldRegion()) if(houseFacet.getWorld(block)) {
            // there should be a house here
            // create a couple 3d regions to help iterate through the cube shape, inside and out
            int extent = 3;
            block.add(0, extent, 0);
            Region3i walls = Region3i.createFromMinMax(
                    new Vector3i(block.x-extent,block.y-extent,block.z-extent),
                    new Vector3i(block.x+extent,block.y+extent-1,block.z+extent));
            Region3i inside = Region3i.createFromMinMax(
                    new Vector3i(block.x-extent+1,block.y-extent,block.z-extent+1),
                    new Vector3i(block.x+extent-1,block.y+extent-1,block.z+extent-1));
            Region3i cieling = Region3i.createFromMinMax(
                    new Vector3i(block.x-extent,block.y+extent,block.z-extent),
                    new Vector3i(block.x+extent,block.y+extent,block.z+extent));

            // loop through each of the positions in the cube, ignoring the is
            for (Vector3i newBlockPosition : inside) {
                chunk.setBlock(ChunkMath.calcBlockPos(newBlockPosition), air);
            }
            for (Vector3i newBlockPosition : walls) {
                if (chunkRegion.getRegion().encompasses(newBlockPosition)
                        && !inside.encompasses(newBlockPosition)) {
                    chunk.setBlock(ChunkMath.calcBlockPos(newBlockPosition), log);
                }
            }
            for (Vector3i newBlockPosition : cieling) {
                chunk.setBlock(ChunkMath.calcBlockPos(newBlockPosition), stone);
            }
            
            Rect2i floor = Rect2i.createFromMinAndMax(block.x-extent, block.z-extent, block.x+extent, block.z+extent);
            for(BaseVector2i position : floor.contents()) {
                int surfaceHeight = (int) surfaceHeightFacet.getWorld(position);
                for(int y=block.y-extent-1; y>surfaceHeight && y>=chunkRegion.getRegion().minY(); y--) {
                    chunk.setBlock(ChunkMath.calcBlockPos(new Vector3i(position.x(),y,position.y())), ash);
                }
            }
            
            chunk.setBlock(ChunkMath.calcBlockPos(new Vector3i(block.x+extent,block.y-extent+1,block.z)), air);
            chunk.setBlock(ChunkMath.calcBlockPos(new Vector3i(block.x+extent,block.y-extent,block.z)), air);

            chunk.setBlock(ChunkMath.calcBlockPos(new Vector3i(block.x-extent,block.y-extent+1,block.z)), air);
            chunk.setBlock(ChunkMath.calcBlockPos(new Vector3i(block.x-extent,block.y-extent,block.z)), air);
            
            chunk.setBlock(ChunkMath.calcBlockPos(new Vector3i(block.x,block.y-extent+1,block.z+extent)), air);
            chunk.setBlock(ChunkMath.calcBlockPos(new Vector3i(block.x,block.y-extent,block.z+extent)), air);

            chunk.setBlock(ChunkMath.calcBlockPos(new Vector3i(block.x,block.y-extent+1,block.z-extent)), air);
            chunk.setBlock(ChunkMath.calcBlockPos(new Vector3i(block.x,block.y-extent,block.z-extent)), air);
        }
    }
}
