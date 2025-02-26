import java.io.*;
import java.util.*;

public class NanoProcessorSim {
    private static final int MEMORY_SIZE = 20;
    private static final String[] memory = new String[MEMORY_SIZE];
    private static int PC = 0; // Program Counter
    private static int AC = 0; // Accumulator
    private static String IR = "0000"; // Instruction Register
    
    public static void main(String[] args) {
        Arrays.fill(memory, "0000"); // Initialize memory with "0000"
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("File with memory contents: ");
        String filename = scanner.nextLine();
        loadMemory(filename);
        
        System.out.print("Enter G to run the program from start to end or S to run it step by step: ");
        char mode = scanner.next().charAt(0);
        
        if (mode == 'G' || mode == 'g') {
            executeProgram(false);
        } else {
            executeProgram(true);
        }
    }
    
    private static void loadMemory(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int address = 0;
            while ((line = br.readLine()) != null && address < MEMORY_SIZE) {
                memory[address++] = line.trim().toUpperCase();
            }
            System.out.println("Memory loaded");
        } catch (IOException e) {
            System.out.println("Error loading memory from file.");
        }
    }
    
    private static void executeProgram(boolean stepMode) {
        Scanner scanner = new Scanner(System.in);
        
        while (PC < MEMORY_SIZE) {
            IR = memory[PC];
            System.out.println("Fetching instruction at address " + formatHex(PC) + " [" + IR + "]");
            
            int opcode = Integer.parseInt(IR.substring(0, 1), 16);
            int operand = Integer.parseInt(IR.substring(1), 16);
            
            switch (opcode) {
                case 1: // Load
                    System.out.println("Decoding instruction: Load from address " + formatHex(operand) + " [" + memory[operand] + "]");
                    AC = Integer.parseInt(memory[operand], 16);
                    break;
                case 2: // Store
                    System.out.println("Decoding instruction: Store to address " + formatHex(operand));
                    memory[operand] = formatHex(AC);
                    break;
                case 5: // Add
                    System.out.println("Decoding instruction: Add from address " + formatHex(operand) + " [" + memory[operand] + "]");
                    AC += Integer.parseInt(memory[operand], 16);
                    AC &= 0xFFFF; // Limit to 16 bits
                    break;
                case 4: // Clear
                    System.out.println("Decoding instruction: Clear AC");
                    AC = 0;
                    break;
                case 6: // Multiply
                    System.out.println("Decoding instruction: Multiply by constant [" + formatHex(operand) + "]");
                    AC *= operand;
                    AC &= 0xFFFF; // Limit to 16 bits
                    break;
                case 0: // Stop
                    System.out.println("Decoding instruction: Stop execution");
                    displayState();
                    System.out.println("End of execution");
                    return;
                default:
                    System.out.println("Unknown instruction");
            }
            
            PC++;
            displayState();
            
            if (stepMode) {
                System.out.println("Press Enter to fetch/decode/execute next instruction.");
                scanner.nextLine();
            }
        }
        
        System.out.println("End of execution");
    }
    
    private static void displayState() {
        System.out.println("\nCPU Registers");
        System.out.println("[" + formatHex(PC) + "] PC");
        System.out.println("[" + formatHex(AC) + "] AC");
        System.out.println("[" + IR + "] IR");
        
        System.out.println("\nMemory");
        for (int i = 0; i < MEMORY_SIZE; i++) {
            System.out.printf("%02X  [%s]\n", i, memory[i]);
        }
    }
    
    private static String formatHex(int value) {
        return String.format("%04X", value & 0xFFFF);
    }
}

//Esto es un comentario
