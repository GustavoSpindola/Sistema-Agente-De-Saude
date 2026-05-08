package saude;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorArquivos {
    private static final String ARQUIVO = "pacientes.dat";

    public static void salvar(List<Paciente> pacientes) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            oos.writeObject(new ArrayList<>(pacientes));
            System.out.println("Dados salvos com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Paciente> carregar() {
        File f = new File(ARQUIVO);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO))) {
            return (List<Paciente>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}