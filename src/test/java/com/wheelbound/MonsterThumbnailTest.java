package com.wheelbound;

import java.awt.image.BufferedImage;
import java.util.List;
import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.Model;
import net.runelite.api.StructComposition;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.game.SpriteManager;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MonsterThumbnailTest
{
    @Test public void monsterArtworkRetriesMissingModelsAndDoesNotMutateGameGeometry()
    {
        Client client = mock(Client.class);
        EnumComposition definitions = mock(EnumComposition.class);
        StructComposition definition = mock(StructComposition.class);
        when(client.getEnum(3987)).thenReturn(definitions);
        when(definitions.getIntVals()).thenReturn(new int[]{100});
        when(client.getStructComposition(100)).thenReturn(definition);
        when(definition.getIntValue(1315)).thenReturn(42);
        when(definition.getIntValue(1322)).thenReturn(200);
        Model model = mock(Model.class);
        float[] x = {-50, 50, 0}, y = {0, 0, -100}, z = {0, 0, 0};
        when(model.getVerticesCount()).thenReturn(3); when(model.getFaceCount()).thenReturn(1);
        when(model.getVerticesX()).thenReturn(x); when(model.getVerticesY()).thenReturn(y); when(model.getVerticesZ()).thenReturn(z);
        when(model.getFaceIndices1()).thenReturn(new int[]{0}); when(model.getFaceIndices2()).thenReturn(new int[]{1});
        when(model.getFaceIndices3()).thenReturn(new int[]{2}); when(model.getFaceColors1()).thenReturn(new int[]{5000});
        when(model.getFaceColors3()).thenReturn(new int[]{5000});
        when(client.loadModel(200)).thenReturn(null, model);
        WheelIconProvider provider = new WheelIconProvider(client, mock(SpriteManager.class), new SkillIconManager());
        CaEncounter monster = new CaEncounter(42, "Bloodveld", List.of());
        assertNull(provider.encounter(monster).icon);
        BufferedImage image = provider.encounter(monster).icon;
        assertNotNull(image); assertEquals(64, image.getWidth());
        assertNotEquals("Thumbnail contains a visible face", 0, image.getRGB(32, 32) >>> 24);
        assertSame(image, provider.encounter(monster).icon);
        verify(client, times(2)).loadModel(200);
        assertArrayEquals(new float[]{-50, 50, 0}, x, 0);
        assertArrayEquals(new float[]{0, 0, -100}, y, 0);
        verify(client, never()).getRasterizer();
    }

    @Test public void bossCaEncountersKeepExistingBossSprites()
    {
        SpriteManager sprites = mock(SpriteManager.class);
        Client client = mock(Client.class);
        CaEncounter obor = new CaEncounter(1, "Obor", List.of());
        BufferedImage sprite = new BufferedImage(25, 25, BufferedImage.TYPE_INT_ARGB);
        when(sprites.getSprite(obor.boss.spriteId, 0)).thenReturn(sprite);
        WheelIconProvider provider = new WheelIconProvider(client, sprites, new SkillIconManager());
        assertSame(sprite, provider.encounter(obor).icon);
        verifyNoInteractions(client);
    }
}
