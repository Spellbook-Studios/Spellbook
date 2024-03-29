package dk.sebsa.spellbook;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.audio.SoundPlayer;
import dk.sebsa.spellbook.core.Application;
import dk.sebsa.spellbook.core.threading.TaskGroup;
import dk.sebsa.spellbook.data.DataStore;
import dk.sebsa.spellbook.data.DataStoreManager;
import dk.sebsa.spellbook.ecs.ECS;
import dk.sebsa.spellbook.ecs.Entity;
import dk.sebsa.spellbook.graphics.opengl.components.SpriteRenderer;
import dk.sebsa.spellbook.imgui.ImGUILayer;
import dk.sebsa.spellbook.phys.components.CircleCollider2D;
import dk.sebsa.spellbook.phys.components.SpriteCollider2D;
import dk.sebsa.spellbook.util.Random;
import imgui.ImGui;
import imgui.flag.ImGuiTableFlags;
import imgui.type.ImFloat;
import imgui.type.ImString;

import java.util.ArrayList;
import java.util.List;

public class DebugLayer extends ImGUILayer {
    private final DataStore data;
    private final TestData testObject;
    private final ImFloat imFloat = new ImFloat();
    private final ImString imString = new ImString();
    public List<TaskGroup> groups = new ArrayList<>();
    public Entity thousandObjectRoot;
    public DebugLayer(Application application) {
        data = DataStoreManager.getFileStore(application, new Identifier("sandbox", "test-data"));
        testObject = data.getOrDefaultObject(new Identifier("test-data", "obj"), () -> new TestData("test"));
    }

    @Override
    protected void drawImGUI() {
        if (ImGui.begin("Sandbox Controls")) {
            if (ImGui.button("Spawn Block")) {
                Entity entity = new Entity(ECS.ROOT);
                SpriteRenderer sr = new SpriteRenderer(new Identifier("sandbox", "32.spr"));
                entity.addComponent(sr);
                entity.addComponent(new SpriteCollider2D(sr));
                entity.transform.setPosition(Random.getFloat(-430, 430), Random.getFloat(-200, 200), 0);
            }
            if (ImGui.button("Spawn Jukebox")) {
                Entity entity = new Entity(ECS.ROOT);
                SpriteRenderer spriteRenderer = (SpriteRenderer) entity.addComponent(new SpriteRenderer(new Identifier("sandbox", "32.spr")));
                entity.addComponent(new CircleCollider2D());

                SoundPlayer sound = (SoundPlayer) entity.addComponent(new SoundPlayer(new Identifier("sandbox", "fantasymphony-wcb.ogg")));
                sound.loop = true;

                entity.transform.setPosition(100, 100, 0);
                sound.start();
            }

            if (ImGui.button("Add Circle Collider")) {
                CircleCollider2D c = new CircleCollider2D();
                c.radius = 32;

                ECS.ROOT.searchChildren("Player").addComponent(c);
            }


            if (ImGui.button("Add Sprite Collider")) {
                Entity player = ECS.ROOT.searchChildren("Player");
                player.addComponent(new SpriteCollider2D(player.getComponent(SpriteRenderer.class)));
            }

            if (ImGui.button("Reset Colliders")) {
                Entity player = ECS.ROOT.searchChildren("Player");
                player.removeComponent(player.getComponent(CircleCollider2D.class));
                player.removeComponent(player.getComponent(SpriteCollider2D.class));
            }

            ImGui.end();
        }

        if (ImGui.begin("Thousand Object Test")) {
            if (thousandObjectRoot == null) {
                if (ImGui.button("Spawn Root")) {
                    thousandObjectRoot = new Entity(ECS.ROOT);
                }
            } else {
                if (ImGui.button("Destroy Root")) {
                    thousandObjectRoot.delete();
                    thousandObjectRoot = null;
                }
            }

            ImGui.beginDisabled(thousandObjectRoot == null);
            ImGui.sameLine();
            if (ImGui.button("Spawn 1000x")) {
                for (int i = 0; i < 1000; i++) {
                    Entity entity = new Entity(thousandObjectRoot);
                    SpriteRenderer sr = new SpriteRenderer(new Identifier("sandbox", "32.spr"));
                    entity.addComponent(sr);
                    entity.transform.setPosition(Random.getFloat(-430, 430), Random.getFloat(-200, 200), 0);
                }
            }
            ImGui.endDisabled();

            ImGui.end();
        }

        if (!Spellbook.instance.getCapabilities().disableThreading && ImGui.begin("Thread Testing")) {
            if (ImGui.button("1000x Print")) {
                TaskGroup.TaskGroupBuilder b = TaskGroup.builder();

                for (int i = 0; i < 1000; i++) {
                    b.addTask(new WaitAndPrintTask("IT FUCKING CUCKING WORKS! nr. " + i));
                }
                groups.add(Spellbook.instance.getTaskManager().run(b.build()));
            }

            if (ImGui.beginTable("##", 3, ImGuiTableFlags.ScrollY | ImGuiTableFlags.Borders | ImGuiTableFlags.RowBg)) {
                ImGui.tableSetupColumn("Started");
                ImGui.tableSetupColumn("Size Left");
                ImGui.tableSetupColumn("IsDone");
                ImGui.tableHeadersRow();

                for (TaskGroup g : groups) {
                    ImGui.tableNextColumn();
                    ImGui.text(String.valueOf(g.startTime));
                    ImGui.tableNextColumn();
                    ImGui.text(String.valueOf(g.tasks.size()));
                    ImGui.tableNextColumn();
                    ImGui.text(String.valueOf(g.isDone()));
                }

                ImGui.endTable();
            }
            ImGui.end();
        }

        if (ImGui.begin("Player")) {
            ImGui.text(Sandbox.player.transform.getGlobalPosition().toString());
            ImGui.end();
        }

        if (ImGui.begin("SaveData test")) {
            ImGui.text(testObject.s);
            ImGui.sameLine();
            ImGui.inputText("S", imString);
            ImGui.sameLine();
            if (ImGui.button("StoreS")) {
                data.storeObject(new Identifier("test-data", "obj"), testObject.setS(imString.get()));
            }

            // Float
            ImGui.text(String.valueOf(data.getOrDefaultFloat(new Identifier("test-data", "float"), 6.9f)));
            ImGui.sameLine();
            ImGui.inputFloat("F", imFloat);
            ImGui.sameLine();
            if (ImGui.button("StoreF")) {
                data.storeFloat(new Identifier("test-data", "float"), imFloat.get());
            }

            ImGui.end();
        }
    }

    @Override
    protected boolean disableDefaultWindows() {
        return false;
    }
}
