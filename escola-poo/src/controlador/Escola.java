package controlador;

import java.util.ArrayList;
import java.util.UUID;

import horario.Horario;
import modelo.*;
import modelo.Output.OutputFactory;

public class Escola{
	private ArrayList<Aluno> alunos;
	private ArrayList<Professor> professores;
	private ArrayList<Turma> turmas;
	private ArrayList<Disciplina> disciplinas;
	private BibliotecaEscolar biblioteca;
        private Notas notas;
        private final OutputFactory outputFactory;
        private String tipoOutput;

	public Escola(OutputFactory outputFactory, String tipoOutput){
		this.alunos = new ArrayList<>();
		this.professores = new ArrayList<>();
		this.turmas = new ArrayList<>();
		this.disciplinas = new ArrayList<>();
		this.biblioteca = new BibliotecaEscolar();
                this.notas = Notas.getInstance(disciplinas);
                this.outputFactory = outputFactory;
                this.tipoOutput = tipoOutput;
	}

	public ArrayList<Turma> getTodasTurmas(){
		return this.turmas;
	}

	public ArrayList<Aluno> getTodosAlunos(){
		return this.alunos;
	}

	public ArrayList<Professor> getTodosProfessores(){
		return this.professores;
	}
        
        public ArrayList<Professor> getProfessoresDisciplina(String nomeDisciplina){
            Disciplina d = getDisciplinaNome(nomeDisciplina);
            
            if(d == null)
                return null;
            
            return d.getProfessores();
        }

	public ArrayList<Disciplina> getTodasDisciplinas(){
		return this.disciplinas;
	}

	public Disciplina getDisciplinaNome(String nome){
		Disciplina temp;
		for(int i=0; i < this.disciplinas.size(); i++){
			temp = this.disciplinas.get(i);
			if(temp.getNome().equals(nome)){
				return temp;
			}
		}
		return null;
	}

	public BibliotecaEscolar getBiblioteca(){
		return this.biblioteca;
	}

	public Turma getTurmaId(String idTurma){
		Turma temp;
		for(int i=0; i < this.turmas.size(); i++){
			temp = this.turmas.get(i);
			if(temp.getID().equals(idTurma)){
				return temp;
			}
		}
		return null;
	}

	public Aluno getAlunoMatricula(String matricula){
		Aluno temp;
		for(int i=0; i < this.alunos.size(); i++){
			temp = this.alunos.get(i);
			if(temp.getMatricula().equals(matricula)){
				return temp;
			}
		}
		return null;
	}

	public Professor getProfessorId(String idProfessor){
		Professor temp;
		for(int i=0; i < this.professores.size(); i++){
			temp = this.professores.get(i);
			if(temp.getID().equals(idProfessor)){
				return temp;
			}
		}
		return null;
	}

	public void addProfessor(String nome, String titulacao){
		UUID id = UUID.randomUUID();
		Logger logger = Logger.getInstance();

		Professor novo = new Professor();

		novo.setID(id.toString());
		novo.setNome(nome);
		novo.setTitulacao(titulacao);

		this.professores.add(novo);
		logger.gravaArquivo(String.format("Professor %s adicionado", nome), Logger.Level.INFO);
	}

	public void addDisciplina(String nome, String unidadeEscolar, String anoEscolar){
		Logger logger = Logger.getInstance();

		Disciplina nova = new Disciplina(outputFactory, nome, unidadeEscolar, anoEscolar, tipoOutput, null);
		this.disciplinas.add(nova);
		logger.gravaArquivo(String.format("Disciplina %s para o ano escolar '%s' adicionada", nome, anoEscolar), Logger.Level.INFO);
	}
        
	public void addTurma(String nome, int quantidadeVagas){
		UUID id = UUID.randomUUID();
		Logger logger = Logger.getInstance();

		Turma novo = new Turma(nome, id.toString(), quantidadeVagas, null);

		logger.gravaArquivo(String.format("Turma %s adicionado", nome), Logger.Level.INFO);
		this.turmas.add(novo);
	}

	public void addAlunoTurma(String nome, String idTurma){
		UUID id = UUID.randomUUID();
		UUID matricula = UUID.randomUUID();
		Logger logger = Logger.getInstance();

		Aluno novo = new Aluno(outputFactory, nome, id.toString(), matricula.toString(), "teste", tipoOutput);

		this.alunos.add(novo);

		System.out.print("ID: "+idTurma+"\n");
		Turma turma = this.getTurmaId(idTurma);

		if(turma != null){
			turma.adicionarAluno(novo);
			logger.gravaArquivo(String.format("Aluno '%s' adicionado na turma de id '%s'", nome, idTurma), Logger.Level.INFO);
		}else{
			logger.gravaArquivo(String.format("Aluno '%s' não adicionado, turma de id '%s' inexistente", nome, idTurma), Logger.Level.ERROR);
		}
	}

	public void trocaAlunoTurma(String matricula, String idTurma){
		Aluno aluno = this.getAlunoMatricula(matricula);
		Turma turma = this.getTurmaId(idTurma);
		Logger logger = Logger.getInstance();

		if(aluno != null && turma != null){
			turma.adicionarAluno(aluno);
			logger.gravaArquivo(String.format("Aluno de matricula %s trocou para a turma de id '%s'", matricula, idTurma), Logger.Level.INFO);
		}else{
			logger.gravaArquivo(String.format("Aluno de matricula '%s' não trocou para a turma de id '%s'", matricula, idTurma), Logger.Level.ERROR);
		}
	}

	public void addDisciplinaTurma(String nome, String idTurma){
		Disciplina disciplina = this.getDisciplinaNome(nome);
		Turma turma = this.getTurmaId(idTurma);
		Logger logger = Logger.getInstance();

		if(disciplina != null && turma != null){
			turma.adicionarDisciplinas(disciplina);
			logger.gravaArquivo(String.format("Disciplina %s adicionada à turma %s", nome, turma.getNomeTurma()), Logger.Level.INFO);
		}else{
			logger.gravaArquivo(String.format("Disciplina %s não adicionada à turma", nome, turma.getNomeTurma()), Logger.Level.ERROR);
		}
	}

	public void removerDisciplinaTurma(String nome, String idTurma){
		Disciplina disciplina = this.getDisciplinaNome(nome);
		Turma turma = this.getTurmaId(idTurma);

		if(disciplina != null && turma != null){
			turma.removerDisciplina(disciplina);
		}
	}

	public void removerTurma(String idTurma) {
		Logger logger = Logger.getInstance();
		int i;

		for(Turma turma : turmas) {
			if(turma.getID().equals(idTurma)) {
				i=0;

				while(!turma.getDisciplinas().isEmpty()) {
					turma.removerDisciplina(turma.getDisciplinas().get(i));
					i++;
				}
				
				turma.setAlunos(null);
				turma.getHorario().removerTudo();
				turmas.remove(turma);
				logger.gravaArquivo(String.format("Turma %s removida", turma.getNomeTurma()), Logger.Level.INFO);

				break;
			}
		}
	}

	public void addHorarioTurma(Horario h, String idTurma){
		Turma turma = this.getTurmaId(idTurma);

		if(turma != null){
			turma.setHorario(h);
		}
	}

	public void removerHorarioTurma(String idTurma){
		Turma turma = this.getTurmaId(idTurma);

		if(turma != null){
			turma.getHorario().removerTudo();
            turma.setHorario(null);
		}
	}

	public void addLivroBiblioteca(String titulo, String autor, String isbn, String genero){
		Livro novo = new Livro(titulo, autor, isbn, genero);
		Logger logger = Logger.getInstance();

		biblioteca.addLivro(novo);
		logger.gravaArquivo(String.format("Livro '%s' de '%s' foi adicionado à biblioteca", titulo, autor), Logger.Level.INFO);
	}

	public void addProfessorBiblioteca(String idProfessor){
		Professor temp = this.getProfessorId(idProfessor);
		if(temp != null){
			biblioteca.addUsuario(temp);
		}
	}

	public void addAlunoBiblioteca(String matricula){
		Aluno temp = this.getAlunoMatricula(matricula);
		if(temp != null){
			biblioteca.addUsuario(temp);
		}
	}

	/*
	public void emprestimoAluno(Livro livro, String matricula){
		Aluno temp = this.getAlunoMatricula(matricula);
		if(temp != null){
			biblioteca.emprestimo(livro, temp);
		}
	}

	public void emprestimoProfessor(Livro livro, String idProfessor){
		Professor temp = this.getProfessorId(idProfessor);
		if(temp != null){
			biblioteca.emprestimo(livro, temp);
		}
	}

	public void devolucaoAluno(Livro livro, String matricula){
		Aluno temp = this.getAlunoMatricula(matricula);
		if(temp != null){
			biblioteca.devolucao(livro, temp);
		}
	}

	public void devolucaoProfessor(Livro livro, String idProfessor){
		Professor temp = this.getProfessorId(idProfessor);
		if(temp != null){
			biblioteca.emprestimo(livro, temp);
		}
	}
	*/

	/**
	 * Método para adicionar uma atividade extra curricular a um aluno
	 * @param matricula 
	 * @param ic
	 * 
	 */
	public void addAtividadeExtraCurricular(String matricula, AtividadeExtra ic){
		Aluno temp = this.getAlunoMatricula(matricula);
		if(temp != null){
			temp.addAtividadeExtra(ic);
		}
	}

        
        //Chamadas metodos classe gerenciadora Notas
        
        public void removerProvaDisciplina(String nomeDisciplina, String nomeProfessor, String nomeProva, String nomeTurma, float peso){
            notas.removerProvaDisciplina(nomeDisciplina, nomeProfessor, nomeProva, nomeTurma, peso);
        }
        
		/**
		 * Método para remover um trabalho de uma disciplina	
		 * @param nomeDisciplina
		 * @param nomeProfessor
		 * @param nomeTrabalho
		 * @param nomeTurma
		 * @param peso
		 */
        public void removerTrabalhoDisciplina(String nomeDisciplina, String nomeProfessor, String nomeTrabalho, String nomeTurma, float peso){
            notas.removerTrabalhoDisciplina(nomeDisciplina, nomeProfessor, nomeTrabalho, nomeTurma, peso);
        }

		/**
		 * Método para remover todos os trabalhos de uma disciplina
		 * @param nomeDisciplina
		 * @param nomeProfessor
		 * @param nomeTurma
		 */
		public void removerTodosTrabalhosDisciplina(String nomeDisciplina, String nomeProfessor, String nomeTurma){
			notas.removerTrabalhos(nomeDisciplina, nomeProfessor, nomeTurma);
		}
		 

        
        public void removerPontoExtraDisciplina(String nomeDisciplina, String nomeProfessor, String nomePontoExtra, String nomeTurma, float valorMaximo){
            notas.removerPontoExtraDisciplina(nomeDisciplina, nomeProfessor, nomePontoExtra, nomeTurma, valorMaximo);
        }
        
        public void adicionarProvaDisciplina(String nomeDisciplina, String nomeProfessor, String nomeTurma, String nomeProva,  float peso){
            notas.adicionarProvaDisciplina(nomeDisciplina, nomeProfessor, nomeProva, nomeTurma, peso);
        }
        
        public void adicionarTrabalhoDisciplina(String nomeDisciplina, String nomeProfessor, String nomeTurma, String nomeTrabalho, float peso) {
            notas.adicionarTrabalhoDisciplina(nomeDisciplina, nomeProfessor, nomeTurma, nomeTrabalho, peso);
        }
        
		/**
		 * Método para adicionar um ponto extra a uma disciplina
		 * @param nomeDisciplina
		 * @param nomeProfessor
		 * @param nomePontoExtra
		 * @param nomeTurma
		 * @param valorMaximo
		 */
        public void adicionarPontoExtraDisciplina(String nomeDisciplina, String nomeProfessor, String nomePontoExtra, String nomeTurma, float valorMaximo) {
            notas.adicionarPontoExtraDisciplina(nomeDisciplina, nomeProfessor, nomePontoExtra, nomeTurma, valorMaximo);
        }
        
        public void adicionarNotaProva(String nomeAluno, String nomeDisciplina, String nomeProfessor, String nomeProva, float notaProva, String nomeTurma) {
            notas.adicionarNotaProva(nomeAluno, nomeDisciplina, nomeProfessor, nomeProva, notaProva, nomeTurma);
        }
        
        public void adicionarNotaTrabalho(String nomeAluno, String nomeDisciplina, String nomeProfessor, String nomeTrabalho, float notaTrabalho, String nomeTurma) {
            notas.adicionarNotaTrabalho(nomeAluno, nomeDisciplina, nomeProfessor, nomeTrabalho, notaTrabalho, nomeTurma);
        }
        
        public void adicionarNotaPontoExtra(String nomeAluno, String nomeDisciplina, String nomeProfessor, String nomePontoExtra, String nomeTurma, float valor) {
            notas.adicionarNotaPontoExtra(nomeAluno, nomeDisciplina, nomeProfessor, nomePontoExtra, nomeTurma, valor);
        }
        
        public void removerNotaProva(String nomeAluno, String nomeDisciplina, String nomeProfessor, String nomeProva, float notaProva, String nomeTurma) {
            notas.removerNotaProva(nomeAluno, nomeDisciplina, nomeProfessor, nomeProva, notaProva, nomeTurma);
        }
        
        public void removerNotaTrabalho(String nomeAluno, String nomeDisciplina, String nomeProfessor, String nomeTrabalho, float notaTrabalho, String nomeTurma) {
            notas.removerNotaTrabalho(nomeAluno, nomeDisciplina, nomeProfessor, nomeTrabalho, notaTrabalho, nomeTurma);
        }
        
        public void removerNotaPontoExtra(String nomeAluno, String nomeDisciplina, String nomeProfessor, String nomePontoExtra, String nomeTurma, float valor) {
            notas.removerNotaPontoExtra(nomeAluno, nomeDisciplina, nomeProfessor, nomePontoExtra, nomeTurma, valor);
        }
        
        public String relatorioProfessores(){
            return RelatorioProfessores.relatorioProfessores(professores);
        }
        
        public void relatorioAlunosTurma(Turma t, int opcao){
            RelatorioAlunos.relatorio(t.getAlunos(), opcao);
        }
        
        /**
         * 
         * @param nomeProfessor
         * @param ID 
         * 
         * @Brief: Ao demitir um professor, será removido de todas as disciplinas que ministrava
         */
        public void demitirProfessor(String nomeProfessor, String ID){
            for(Disciplina disciplina : disciplinas){
                ArrayList<Professor> professoresDisciplina = disciplina.getProfessores();
                for(Professor professor : professoresDisciplina){
                    if(professor.getNome().equals(nomeProfessor) && professor.getID().equals(ID)){
                        disciplina.removerProfessor(professor);
						professores.remove(professor);
                    }
                }
            }
        }
        
        /**
         * 
         * @param nomeAluno
         * @param ID 
         * 
         * @Brief: Ao expulsar um aluno, será removido da turma que pertencia
         */
        public void expulsarAluno(String nomeAluno, String ID){
            for(Turma turma : turmas){
                ArrayList<Aluno> alunosTurma = turma.getAlunos();
                for(Aluno aluno : alunosTurma){
                    if(aluno.getNome().equals(nomeAluno) && aluno.getID().equals(ID)){
                        turma.removerAluno(aluno);
                        return;
                    }
                }
            }
        }

		public void addNotasProvaTurma(Turma turma, Disciplina disciplina, String nomeProva, float nota, float peso){
			int i=0;
			for(Aluno aluno : turma.getAlunos()){
				notas.adicionarNotaProva(aluno.getNome(), disciplina.getNome(), disciplina.getProfessores().get(i).getNome(), nomeProva, nota, turma.getNomeTurma());
				i++;
			}
		}

		public void addNotasTrabalhoTurma(Turma turma, Disciplina disciplina, String nomeTrabalho, float nota, float peso){
			int i=0;
			for(Aluno aluno : turma.getAlunos()){
				notas.adicionarNotaTrabalho(aluno.getNome(), disciplina.getNome(), disciplina.getProfessores().get(i).getNome(), nomeTrabalho, nota, turma.getNomeTurma());
				i++;
			}
		}

		public void addNotasPontoExtraTurma(Turma turma, Disciplina disciplina, String nomePontoExtra, float valor){
			int i=0;
			for(Aluno aluno : turma.getAlunos()){
				notas.adicionarNotaPontoExtra(aluno.getNome(), disciplina.getNome(), disciplina.getProfessores().get(i).getNome(), nomePontoExtra, turma.getNomeTurma(), valor);
				i++;
			}
		}
        
}
