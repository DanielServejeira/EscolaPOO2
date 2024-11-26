package Gerenciamento;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import Classes.Disciplina;

/**
 * @Brief: Classe CalendarioEscolar que representa o calend�rio das aulas de uma institui��o, organizando os per�odos e intervalos
 */
public class CalendarioEscolar {

    private static final int DURACAO_AULA_MINUTOS = 50;
    private static final int DURACAO_INTERVALO_MINUTOS_BEFORENOON = 15;
    private static final int DURACAO_INTERVALO_MINUTOS_AFTERNOON= 15;
    private static final LocalTime HORA_INICIO = LocalTime.of(7, 0);
    private static final LocalTime HORA_FINAL = LocalTime.of(15, 40);
    private static final LocalTime HORA_ALMOCO_INICIO = LocalTime.of(12,15);
    private static final LocalTime HORA_ALMOCO_FINAL = LocalTime.of(13,10);
    private int contadorGeralDeAulas = 0;
    private ArrayList<Periodo> periodos;
    private boolean intervalo = false;

     // Mapeia os dias da semana para suas aulas
    private Map<String, List<Periodo>> calendario;
    String[] diasDaSemana = {"Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira"};

    /**
     * @Brief: Construtor da classe que inicializa o calend�rio escolar chamando o m�todo para gerar os per�odos de aula para os dias da semana
     */
    public CalendarioEscolar() {
        calendario = new LinkedHashMap<>();
        inicializarCalendario();
        
    }

    public static int getDURACAO_AULA_MINUTOS() {
        return DURACAO_AULA_MINUTOS;
    }

    public static int getDURACAO_INTERVALO_MINUTOS_BEFORENOON() {
        return DURACAO_INTERVALO_MINUTOS_BEFORENOON;
    }

    public static int getDURACAO_INTERVALO_MINUTOS_AFTERNOON() {
        return DURACAO_INTERVALO_MINUTOS_AFTERNOON;
    }

    public static LocalTime getHORA_INICIO() {
        return HORA_INICIO;
    }

    public static LocalTime getHORA_FINAL() {
        return HORA_FINAL;
    }

    public static LocalTime getHORA_ALMOCO_INICIO() {
        return HORA_ALMOCO_INICIO;
    }

    public static LocalTime getHORA_ALMOCO_FINAL() {
        return HORA_ALMOCO_FINAL;
    }

    public int getContadorGeralDeAulas() {
        return contadorGeralDeAulas;
    }
    
    /**
     * @Brief: Inicializa o calend�rio com os per�odos de aula para cada dia da semana
     */
    private void inicializarCalendario() {
        
        for (String dia : diasDaSemana) {
            calendario.put(dia, gerarPeriodosAula());
        }
    }

    /**
     * @Brief: Gera a lista de per�odos de aula para um dia espec�fico
     * @Return: Lista de per�odos de aula
     */
    private List<Periodo> gerarPeriodosAula() {
        periodos = new ArrayList<>();
        LocalTime inicio = HORA_INICIO;
        int contadorAulas = 0;
        while (inicio.isBefore(HORA_FINAL)) {
            
            //Se o horario de inicio seja o horário do almoço, deve-se pular para o fim desse intervalo
            if((inicio.equals(HORA_ALMOCO_INICIO))){
                inicio = HORA_ALMOCO_FINAL;
                continue;
            }

            contadorAulas++;
            LocalTime fim = inicio.plusMinutes(DURACAO_AULA_MINUTOS);
            if (fim.isAfter(HORA_FINAL)) break;
            
            
                periodos.add(new Periodo(inicio, fim));
            
            inicio = fim;
            
            //Verificar se é hora do intervalo
            if(inicio.isBefore(HORA_ALMOCO_INICIO) && contadorAulas == 3){
                inicio = inicio.plusMinutes(DURACAO_INTERVALO_MINUTOS_BEFORENOON);
                contadorAulas = 0;
            }else if( inicio.isAfter(HORA_ALMOCO_FINAL) && contadorAulas == 2){
                inicio = inicio.plusMinutes(DURACAO_INTERVALO_MINUTOS_AFTERNOON);
                contadorAulas = 0;
            }
            contadorGeralDeAulas++;
        }

        return periodos;
    }

    /**
     * @Brief: Exibe o calend�rio completo com os per�odos e suas disciplinas
     */
    public void exibirCalendario() {
        int conferidor = 0;
        LocalTime horasAux = HORA_INICIO;
        for (Map.Entry<String, List<Periodo>> entrada : calendario.entrySet()) {
            System.out.println(entrada.getKey() + ":");
            for (Periodo periodo : entrada.getValue()) {
               
                
                if(periodo.getDisciplina() == null){
                    if(periodo.getHoraInicio() != horasAux){
                        System.out.println("\tIntervalo\n\t" + periodo);
                    }else System.out.println("\t" + periodo);
                }
                else{
                    if(periodo.getHoraInicio() != horasAux){
                        System.out.println("\tIntervalo\n\t" + periodo + periodo.getDisciplina().getNome());
                    }else System.out.println("\t" + periodo + periodo.getDisciplina().getNome());
                }
                horasAux = periodo.getHoraFim();
            }
            horasAux = HORA_INICIO;
        }
    }
    
    /**
     * @Brief: Adiciona disciplinas ao calend�rio de um dia
     * @Parameter: dia - Dia da semana para adicionar as disciplinas
     * @Parameter: disciplinas - Lista de disciplinas a serem adicionadas
     * @Return: true se as disciplinas foram adicionadas, false caso contr�rio
     */
    public boolean adicionarDisciplina(String dia, ArrayList<Disciplina> disciplinas){
        if(disciplinas.size() > contadorGeralDeAulas) return false;
        int i = 0;
        for (Map.Entry<String, List<Periodo>> entrada : calendario.entrySet()) {
            if(entrada.getKey().equals(dia)){
                for (Periodo per : entrada.getValue()) {
                    per.setDisciplina(disciplinas.get(i++));
                    if(i == disciplinas.size()) break;
                }
            }
        }
        
        return true;
    }
    
    /**
     * @Brief: Adiciona uma disciplina a um per�odo espec�fico
     * @Parameter: disciplina - Disciplina a ser adicionada
     * @Parameter: periodo - Per�odo espec�fico onde a disciplina ser� adicionada
     * @Return: true se a disciplina foi adicionada, false caso contr�rio
     */
    public boolean adicionarDisciplina(Disciplina disciplina, Periodo periodo){
        if(periodo == null) return false;
        if(disciplina == null) return false;
        
        for(int i = 0; i < contadorGeralDeAulas; i++){
           if(this.periodos.get(i).equals(periodo)){
               this.periodos.get(i).setDisciplina(disciplina);
           }
        }
        return true;
    }
    
    /**
     * @Brief: Adiciona uma disciplina ao calend�rio de um dia espec�fico e hor�rio
     * @Parameter: dia - Dia da semana
     * @Parameter: disciplina - Disciplina a ser adicionada
     * @Parameter: inicio - Hora de in�cio do per�odo
     * @Return: true se a disciplina foi adicionada, false caso contr�rio
     */
    public boolean adicionarDisciplina(String dia, Disciplina disciplina, LocalTime inicio){
        if(inicio == null) return false;
        if(disciplina == null) return false;
        
        for (Map.Entry<String, List<Periodo>> entrada : calendario.entrySet()) {
            if(entrada.getKey().equals(dia)){
                for (Periodo per : entrada.getValue()) {
                    if(per.getHoraInicio().equals(inicio)){
                        per.setDisciplina(disciplina);
                    }
                }
            }
        }
        
        return true;
    }
    
    /**
     * @Brief: Remove todas as disciplinas de um dia espec�fico
     * @Parameter: dia - Dia da semana
     * @Return: true se as disciplinas foram removidas, false caso contr�rio
     */
    public boolean removerTodasDisciplinas(String dia){
        for(Map.Entry<String, List<Periodo>> entrada : calendario.entrySet()){
            if(entrada.getKey().equals(dia)){
                for(Periodo per : entrada.getValue()){
                    if(per.getDisciplina() != null){
                        per.setDisciplina(null);
                    }
                }
            }
        }
        
        return true;
    }
    
    /**
     * @Brief: Remove uma disciplina de um per�odo espec�fico de um dia
     * @Parameter: dia - Dia da semana
     * @Parameter: inicio - Hora de in�cio do per�odo
     * @Return: true se a disciplina foi removida, false caso contr�rio
     */
    public boolean removerDisciplinaDia(String dia, LocalTime inicio){
        for(Map.Entry<String, List<Periodo>> entrada: calendario.entrySet()){
            if(entrada.getKey().equals(dia)){
                for(Periodo per : entrada.getValue()){
                    if(per.getHoraInicio().equals(inicio)){
                        per.setDisciplina(null);
                    }
                }
            }
        }
        return true;
    }
}
  
   
