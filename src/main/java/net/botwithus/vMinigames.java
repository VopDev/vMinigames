package net.botwithus;

import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.hud.interfaces.Component;
import net.botwithus.rs3.game.js5.types.vars.VarDomainType;
import net.botwithus.rs3.game.minimenu.MiniMenu;
import net.botwithus.rs3.game.minimenu.actions.ComponentAction;
import net.botwithus.rs3.game.movement.Movement;
import net.botwithus.rs3.game.movement.NavPath;
import net.botwithus.rs3.game.queries.builders.characters.NpcQuery;
import net.botwithus.rs3.game.queries.builders.components.ComponentQuery;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;
import net.botwithus.rs3.game.queries.results.EntityResultSet;
import net.botwithus.rs3.game.scene.entities.characters.npc.Npc;
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer;
import net.botwithus.rs3.game.scene.entities.object.SceneObject;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.LoopingScript;
import net.botwithus.rs3.script.config.ScriptConfig;
import net.botwithus.rs3.events.impl.ChatMessageEvent;
import net.botwithus.rs3.game.Coordinate;
import net.botwithus.rs3.game.vars.VarManager;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class vMinigames extends LoopingScript {

    private BotState botState = BotState.IDLE;
    public int LadyHefinCurrentAnim = 0;
    private Random random = new Random();
    public String CWTeam = null;
    public boolean TeamChecked = false;
    public int CWGamesCompleted = 0;
    public int FlashPowderGamesCompleted = 0;
    public int earnedthaler = 0;
    public int totalthaler = 0;
    public int totalearnedthaler = 0;
    public int currentAreaId = Client.getLocalPlayer().getCoordinate().getRegionId();
    public boolean isDebugMode = false;
    public long starttime = System.currentTimeMillis();

    //AREA IDS
    final int CWARS_GAME_AREA = 9520;
    final int CWARS_GAME_LOBBY = 9620;
    final int CWARS_LOBBY_OVERWORLD = 9776;

    //CWars Booleans
    private boolean afkLocationSet = false; // New flag to track if currentAfkLocation has been set

    enum BotState {
        IDLE,
        CASTLEWARS,
        FLASHPOWDERFACTORY
    }


    public vMinigames(String s, ScriptConfig scriptConfig, ScriptDefinition scriptDefinition) {
        super(s, scriptConfig, scriptDefinition);
        this.sgc = new vMinigamesGraphicsContext(getConsole(), this);

    }

    public String getRuntime() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - starttime;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - TimeUnit.MINUTES.toSeconds(minutes);
        return String.format("%d:%02d", minutes, seconds);
    }


    public boolean initialize() {
        super.initialize();
        subscribe(ChatMessageEvent.class, chatMessageEvent -> {
            if (chatMessageEvent.getMessage().contains("You have earned")) {
                String message = chatMessageEvent.getMessage();
                earnedthaler = Integer.parseInt(message.replaceAll(".*?earned (\\d+) thaler.*", "$1"));
                totalthaler = Integer.parseInt(message.replaceAll(".*?you now have (\\d+).*", "$1"));
                println("[ThalerCount] " + earnedthaler + " thaler earned.");
                totalearnedthaler += earnedthaler;
                println("[ThalerCount] " + totalthaler + " thaler total.");
                setBotState(BotState.IDLE);
            }
            if (chatMessageEvent.getMessage().contains("Your score in the factory last game")) {
               FlashPowderGamesCompleted++;
            }
        });
        return true;
    }

    @Override
    public void onLoop() {
        LocalPlayer player = Client.getLocalPlayer();
        if (player == null || Client.getGameState() != Client.GameState.LOGGED_IN || botState == BotState.IDLE) {
            getCurrentAreaId();
            Execution.delay(random.nextLong(3000,7000));
            return;
        }
        getCurrentAreaId();
        switch (botState) {
            case IDLE -> {
                println("We're idle!");
                Execution.delay(random.nextLong(1000,3000));
            }
            case CASTLEWARS -> {
                Execution.delay(random.nextLong(450,900));
                CastleWars();
                return;
            }
            case FLASHPOWDERFACTORY -> {
                Execution.delay(random.nextLong(450,900));
                FlashPowderFactory();
                return;
            }
        }
    }

 
    // Varbits 

    public int getThalerValue() {
        return VarManager.getVarValue(VarDomainType.PLAYER, 5427); 
    }



    public int getTimerValue() {
        return VarManager.getVarValue(VarDomainType.PLAYER, 1417);
    }


    private Coordinate currentAfkLocation = null;

    public void moveToAFK() {
        LocalPlayer player = Client.getLocalPlayer(); 
        Coordinate zamafk = new Coordinate(2374,3124,1);
        Coordinate zamafk2 = new Coordinate(2368,3126,1);
        Coordinate zamafk3 = new Coordinate(2378,3135,1);
        Coordinate sarafk = new Coordinate(2425,3083,1);
        Coordinate sarafk2 = new Coordinate(2431,3081,1);
        Coordinate sarafk3 = new Coordinate(2421,3072,1);

        if (!afkLocationSet) { 
            currentAfkLocation = CWTeam.equals("Zamorak") ? 
                (random.nextBoolean() ? (random.nextBoolean() ? zamafk : zamafk2) : zamafk3) : 
                (random.nextBoolean() ? (random.nextBoolean() ? sarafk : sarafk2) : sarafk3);
            afkLocationSet = true; 
            if (isDebugMode) {
                println("[DEBUG] AFK location set for game at: " + currentAfkLocation);
            }
        }

        println("[CastleWars] Current AFK Location: " + currentAfkLocation);
        if (player.getCoordinate().equals(currentAfkLocation)) { 
            println("[CastleWars] Already at AFK location - Delaying..."); 
            Execution.delay(random.nextLong(30000, 45000)); 
        } else {
            println("[CastleWars] Moving to AFK location...");
            NavPath path = NavPath.resolve(currentAfkLocation); 
            if (isDebugMode) {
                println("[DEBUG] Moving to coordinates: " + currentAfkLocation.getX() + ", " + currentAfkLocation.getY() + ", " + currentAfkLocation.getZ());
            }
            Movement.traverse(path); 
            println("[CastleWars] Arrived at AFK location.");
            Execution.delay(random.nextLong(30000, 45000)); 
        }
        if (isDebugMode) {
            println("[DEBUG] Completed moveToAFK returning to main CastleWars function.");
        }
        CastleWars();
    }

public void checkCloakEquipped() {
    if (isDebugMode) {
        println("[DEBUG] TeamChecked Variable: " + TeamChecked);
    }
    if (TeamChecked) return;
    Component zamorakResults = ComponentQuery.newQuery(1464).componentIndex(15).itemName("Zamorak cloak").results().first();
    if (zamorakResults != null) {
        println("[CastleWars] Detected Team: Zamorak");
        CWTeam = "Zamorak";

    } else {
        Component saradominResults = ComponentQuery.newQuery(1464).componentIndex(15).itemName("Saradomin cloak").results().first();
        if (saradominResults != null) {
            println("[CastleWars] Detected Team: Saradomin");
            CWTeam = "Saradomin";
        } else {
            println("[CastleWars] No cloak equipped.");
        }
    }
    TeamChecked = true;
}

    public void CastleWars() {
        LocalPlayer player = Client.getLocalPlayer(); 
        int areaid = Client.getLocalPlayer().getCoordinate().getRegionId();
        if (areaid == -1) { 
            if (isDebugMode) {
                println("[DEBUG] No area ID found. Returning...");
            }
            return; 
        }
        if (isDebugMode) {
            println("[DEBUG] Main CW Loop - Current Area ID: " + areaid);
        }

        if (player != null) {
            if (areaid == CWARS_GAME_AREA) { 
                Execution.delay(random.nextLong(1500, 1750));
                checkCloakEquipped();
                moveToAFK();
            }
            if (areaid == CWARS_GAME_LOBBY) { 
                Execution.delay(random.nextLong(3500,4000));
                return;
            }
            if (areaid == CWARS_LOBBY_OVERWORLD) { 
            afkLocationSet = false;
            currentAfkLocation = null; 
            TeamChecked = false;
            CWTeam = null;
            SceneObject guthixPortal = SceneObjectQuery.newQuery().name("Guthix portal").option("Enter").results().nearest();
            Execution.delay(random.nextLong(1000,2000));
            MiniMenu.interact(ComponentAction.COMPONENT.getType(), 1, -1, 64553048);
            CWGamesCompleted++;
            Execution.delay(random.nextLong(1000,2000));
            if (guthixPortal != null) {
                guthixPortal.interact("Enter");
                println("[CastleWars] Interacting with Guthix portal...");
                Execution.delay(random.nextLong(25000,30000));
                return;
            } else {
                println("[CastleWars] Guthix portal not found.");
                return;
            }
            }
        }
    }

    // EntityResultSet<SceneObject> results = SceneObjectQuery.newQuery().name("Zamorak flag").option("Capture").results();
    // zam flag room 2373 3133
    // sar flag room 2428 3078
    //ResultSet<Item> results = InventoryItemQuery.newQuery(94).name("Zamorak flag").option("Remove").results();

    // CASTLE WARS END

    // FLASH POWDER FACTORY

    public void findFallenRubble() {
        SceneObject fallenRubble = SceneObjectQuery.newQuery().name("Fallen rubble").results().nearest();
        if (fallenRubble != null) {
            println("[FlashPowderFactory] Found Fallen Rubble.");
            println("[FlashPowderFactory] Coordinates: " + fallenRubble.getCoordinate());
        } else {
            println("[FlashPowderFactory] Fallen Rubble not found.");
        }
    }

    public void FlashPowderFactory() {
        LocalPlayer player = Client.getLocalPlayer();
        int areaid = Client.getLocalPlayer().getCoordinate().getRegionId();
    if (areaid == 37682 || areaid == 37426) {
        if (isDebugMode) {
            println("[DEBUG] Current Area ID: " + areaid);
        }
        println("[FlashPowderFactory] Waiting - Currently in Flash Powder Factory");
        Execution.delay(random.nextLong(25000,30000));
        return;
    }
    if (areaid == 12109) {
       EntityResultSet<Npc> results = NpcQuery.newQuery().name("Brian O'Richard").option("Quick start").results();
       if (results.size() > 0) {
        Npc brian = results.first();
        brian.interact("Quick start");
        Execution.delay(random.nextLong(1000,2000));
        MiniMenu.interact(ComponentAction.DIALOGUE.getType(), 0, -1, 77594639);
        if (isDebugMode) {
            println("[DEBUG] Interacted with Brian O'Richard: minimenu 0, -1, 77594639");
        }
        Execution.delay(random.nextLong(1000,2000));
        return;
       } else {
        println("[FlashPowderFactory] Brian O'Richard not found.");
        return;
       }
    }
}
// Area 37682 FLash Powder
//Area 12109 Rogues Den

  
    public void getCurrentAreaId() {
        LocalPlayer player = Client.getLocalPlayer(); 
        if (player != null) {
            int areaId = player.getCoordinate().getRegionId(); 
            currentAreaId = areaId;
        } else {
            println("Player is not in a valid area.");
        }
       
    }




























    public String getCurrentTeam() {
        return CWTeam;
    }

    public BotState getBotState() {
        return botState;
    }

    public void setBotState(BotState botState) {
        this.botState = botState;
    }

    public void setDebugMode(boolean isDebugMode) {
        this.isDebugMode = isDebugMode;
    }

    public boolean isDebugMode() {
        return isDebugMode; 
    }

}