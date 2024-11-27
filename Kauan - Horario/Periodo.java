/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gerenciamento;

/**
 *
 * @author kauan
 */
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Classes.Disciplina;

  // Classe interna para representar um periodo de aula
    public  class Periodo {
        private LocalTime horaInicio;
        private LocalTime horaFim;
        private Disciplina disciplina;

        /**
        * @Brief: Construtor para inicializar o per�odo de aula com hora de in�cio, hora de fim e a disciplina
        * @Parameter: horaInicio - Hora de in�cio do per�odo
        * @Parameter: horaFim - Hora de fim do per�odo
        * @Parameter: disciplina - Disciplina associada ao per�odo
        */
        public Periodo(LocalTime horaInicio, LocalTime horaFim, Disciplina disciplina) {
            this.horaInicio = horaInicio;
            this.horaFim = horaFim;
            this.disciplina = disciplina;
        }
        
        /**
        * @Brief: Construtor para inicializar o per�odo de aula com hora de in�cio e hora de fim, mas sem disciplina
        * @Parameter: horaInicio - Hora de in�cio do per�odo
        * @Parameter: horaFim - Hora de fim do per�odo
        */
         public Periodo(LocalTime horaInicio, LocalTime horaFim) {
            this.horaInicio = horaInicio;
            this.horaFim = horaFim;
        }

    /**
     * @Brief: Retorna a disciplina associada ao per�odo
     * @Return: A disciplina associada ao per�odo
     */
    public Disciplina getDisciplina() {
        return disciplina;
    }

    /**
     * @Brief: Retorna a hora de in�cio do per�odo
     * @Return: A hora de in�cio do per�odo
     */
    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    /**
     * @Brief: Define a hora de in�cio do per�odo
     * @Parameter: horaInicio - Hora de in�cio do per�odo
     */
    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    /**
     * @Brief: Retorna a hora de fim do per�odo
     * @Return: A hora de fim do per�odo
     */
    public LocalTime getHoraFim() {
        return horaFim;
    }

    /**
     * @Brief: Define a hora de fim do per�odo
     * @Parameter: horaFim - Hora de fim do per�odo
     */
    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    /**
     * @Brief: Define a disciplina associada ao per�odo
     * @Parameter: disciplina - Disciplina associada ao per�odo
     */
    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }
        /**
        * @Brief: Representa��o em formato String do per�odo de aula
        * @Return: String com o hor�rio e a disciplina do per�odo
        */
        @Override
        public String toString() {
            return horaInicio + " - " + horaFim + ": ";
        }
    }