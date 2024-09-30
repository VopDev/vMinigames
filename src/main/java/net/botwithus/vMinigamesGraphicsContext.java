package net.botwithus;


import net.botwithus.vMinigames.BotState;
import net.botwithus.rs3.imgui.ImGui;
import net.botwithus.rs3.imgui.ImGuiWindowFlag;
import net.botwithus.rs3.script.ScriptConsole;
import net.botwithus.rs3.script.ScriptGraphicsContext;

public class vMinigamesGraphicsContext extends ScriptGraphicsContext {

    private vMinigames script;

    public vMinigamesGraphicsContext(ScriptConsole scriptConsole, vMinigames script) {
        super(scriptConsole);
        this.script = script;
    }

    

    @Override
    public void drawSettings() {
        if (ImGui.Begin("vMinigames - " + script.getBotState(), ImGuiWindowFlag.AlwaysUseWindowPadding.getValue())) {
            ImGui.SetWindowSize(550.0f, 200.0f);
            if (ImGui.BeginTabBar("vMinigames", ImGuiWindowFlag.None.getValue())) {
                if (ImGui.BeginTabItem("Castle Wars", ImGuiWindowFlag.None.getValue())) {
                    ImGui.Separator();
                    if (script.currentAreaId == 9520) {
                        ImGui.Text("In Game: " + script.getTimerValue() + " minutes remaining");
                    } else if (script.currentAreaId == 9620) {
                        ImGui.Text("In Game Lobby: " + script.getTimerValue() + " minutes remaining");
                    } else {
                        ImGui.Text("Castle Wars");
                    }
                    ImGui.SameLine();
                    ImGui.Text("||  Current Thaler: " + script.getThalerValue());
                    ImGui.SetTooltip("Earned This Session: " + script.totalearnedthaler);
                    ImGui.SameLine();
                    ImGui.Text(" ||  Runtime: " + script.getRuntime());
                    ImGui.Separator();
                    String currentTeam = script.getCurrentTeam();
                    if (currentTeam != null && !currentTeam.isEmpty()) {
                        ImGui.Text("Current Team: " + currentTeam);
                    }
                    if (script.CWGamesCompleted > 0) {
                        ImGui.Text("Games: " + script.CWGamesCompleted);
                    }
                    ImGui.Text("");
                    ImGui.PushStyleColor(ImGuiCol.Button, 0xFF00FF00); 
                    if (ImGui.Button("Start")) {
                        script.setBotState(BotState.CASTLEWARS);
                    }
                    ImGui.PopStyleColor(); 

                    ImGui.SameLine();

                    ImGui.PushStyleColor(ImGuiCol.Button, 1.0f, 0.0f, 0.0f, 1.0f); 
                    if (ImGui.Button("Stop")) {
                        script.setBotState(BotState.IDLE);
                    }
                    ImGui.PopStyleColor(); 
                    ImGui.EndTabItem();
                  
                }
                if (ImGui.BeginTabItem("Flash Powder Factory", ImGuiWindowFlag.None.getValue())) {
                    ImGui.EndTabItem();
                    ImGui.Separator();
                    if (script.currentAreaId == 12109) {
                        ImGui.Text("In Rogues Den");
                    } else if (script.currentAreaId == 37682 || script.currentAreaId == 37426) {
                        ImGui.Text("In Flash Powder Factory");
                    } else {
                        ImGui.Text("Flash Powder Factory");
                    }
                    ImGui.SameLine();
                    ImGui.Text("||  Current Thaler: " + script.getThalerValue());
                    ImGui.SetTooltip("Earned This Session: " + script.totalearnedthaler);
                    ImGui.SameLine();
                    ImGui.Text(" ||  Runtime: " + script.getRuntime());
                    ImGui.Separator();
                    ImGui.Text("Games Completed: " + script.FlashPowderGamesCompleted);
                    ImGui.Text("");
                    ImGui.PushStyleColor(ImGuiCol.Button, 0xFF00FF00); 
                    if (ImGui.Button("Start")) {
                        script.setBotState(BotState.FLASHPOWDERFACTORY);
                    }
                    ImGui.PopStyleColor(); 

                    ImGui.SameLine();

                    ImGui.PushStyleColor(ImGuiCol.Button, 1.0f, 0.0f, 0.0f, 1.0f); 
                    if (ImGui.Button("Stop")) {
                        script.setBotState(BotState.IDLE);
                    }
                    ImGui.PopStyleColor();
                }
                if (ImGui.BeginTabItem("Extra Settings", ImGuiWindowFlag.None.getValue())) {
                    script.setDebugMode(ImGui.Checkbox("Debug Mode", script.isDebugMode()));
                    ImGui.SetTooltip("Outputs additional information to the console for debugging & error reporting.");
                    ImGui.EndTabItem();
                  
                }
                   
                    
            };
            
                   
               
                
            }
            ImGui.End(); 
        }
    

    @Override
    public void drawOverlay() {
        super.drawOverlay();
    }

    public final class ImGuiCol {
        private ImGuiCol() {
        }

        public static final int Text = 0;
        public static final int TextDisabled = 1;
        public static final int WindowBg = 2;
        public static final int ChildBg = 3;
        public static final int PopupBg = 4;
        public static final int Border = 5;
        public static final int BorderShadow = 6;
        public static final int FrameBg = 7;
        public static final int FrameBgHovered = 8;
        public static final int FrameBgActive = 9;
        public static final int TitleBg = 10;
        public static final int TitleBgActive = 11;
        public static final int TitleBgCollapsed = 12;
        public static final int MenuBarBg = 13;
        public static final int ScrollbarBg = 14;
        public static final int ScrollbarGrab = 15;
        public static final int ScrollbarGrabHovered = 16;
        public static final int ScrollbarGrabActive = 17;
        public static final int CheckMark = 18;
        public static final int SliderGrab = 19;
        public static final int SliderGrabActive = 20;
        public static final int Button = 21;
        public static final int ButtonHovered = 22;
        public static final int ButtonActive = 23;
        public static final int Header = 24;
        public static final int HeaderHovered = 25;
        public static final int HeaderActive = 26;
        public static final int Separator = 27;
        public static final int SeparatorHovered = 28;
        public static final int SeparatorActive = 29;
        public static final int ResizeGrip = 30;
        public static final int ResizeGripHovered = 31;
        public static final int ResizeGripActive = 32;
        public static final int Tab = 33;
        public static final int TabHovered = 34;
        public static final int TabActive = 35;
        public static final int TabUnfocused = 36;
        public static final int TabUnfocusedActive = 37;
        public static final int DockingPreview = 38;
        public static final int DockingEmptyBg = 39;
        public static final int PlotLines = 40;
        public static final int PlotLinesHovered = 41;
        public static final int PlotHistogram = 42;
        public static final int PlotHistogramHovered = 43;
        public static final int TableHeaderBg = 44;
        public static final int TableBorderStrong = 45;
        public static final int TableBorderLight = 46;
        public static final int TableRowBg = 47;
        public static final int TableRowBgAlt = 48;
        public static final int TextSelectedBg = 49;
        public static final int DragDropTarget = 50;
        public static final int NavHighlight = 51;
        public static final int NavWindowingHighlight = 52;
        public static final int NavWindowingDimBg = 53;
        public static final int ModalWindowDimBg = 54;
    }
    public final class ImGuiStyleVar {
        private ImGuiStyleVar() {
        }

        public static final int Alpha = 0;
        public static final int WindowPadding = 1;
        public static final int WindowRounding = 2;
        public static final int WindowBorderSize = 3;
        public static final int WindowMinSize = 4;
        public static final int WindowTitleAlign = 5;
        public static final int ChildRounding = 6;
        public static final int ChildBorderSize = 7;
        public static final int PopupRounding = 8;
        public static final int PopupBorderSize = 9;
        public static final int FramePadding = 10;
        public static final int FrameRounding = 11;
        public static final int FrameBorderSize = 12;
        public static final int ItemSpacing = 13;
        public static final int ItemInnerSpacing = 14;
        public static final int IndentSpacing = 15;
        public static final int CellPadding = 16;
        public static final int ScrollbarSize = 17;
        public static final int ScrollbarRounding = 18;
        public static final int GrabMinSize = 19;
        public static final int GrabRounding = 20;
        public static final int TabRounding = 21;
        public static final int ButtonTextAlign = 22;
        public static final int SelectableTextAlign = 23;
    }
}