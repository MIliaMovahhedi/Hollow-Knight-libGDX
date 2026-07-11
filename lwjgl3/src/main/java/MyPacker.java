import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class MyPacker {
    public static void main(String[] args) {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxWidth = 2048;
        settings.maxHeight = 2048;

        TexturePacker.process(settings,
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\raw_knight_frames",
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\knight_atlas",
            "knight"
        );

        TexturePacker.process(settings,
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\raw_HUD_frames",
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\HUD_atlas",
            "hud"
        );

        TexturePacker.process(settings,
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\raw_effects_frames",
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\effects_atlas",
            "effects"
        );

        TexturePacker.process(settings,
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\raw_enemies_frames\\raw_crawlid_frames",
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\enemies_atlas",
            "crawlid"
        );

        TexturePacker.process(settings,
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\raw_enemies_frames\\raw_mossfly_frames",
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\enemies_atlas",
            "mossfly"
        );

        TexturePacker.process(settings,
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\raw_enemies_frames\\raw_husk_frames",
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\enemies_atlas",
            "husk"
        );

        TexturePacker.process(settings,
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\raw_enemies_frames\\raw_guardian_frames",
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\enemies_atlas",
            "guardian"
        );

        TexturePacker.process(settings,
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\raw_zote_frames",
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\npc_atlas",
            "zote"
        );

        TexturePacker.process(settings,
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\raw_enemies_frames\\raw_boss_frames",
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\enemies_atlas",
            "falseKnight"
        );
    }
}
