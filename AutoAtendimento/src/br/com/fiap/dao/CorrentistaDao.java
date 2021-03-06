package br.com.fiap.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import br.com.fiap.model.Correntista;

/**
 * Classe responsavel por manipular os dados do correntista nos arquivos de
 * texto
 *
 */

public class CorrentistaDao {
	/**
	 * Metodo responsavel por recuperar os dados do cliente de seu respectivo arquivo
	 * 
	 * @param cpf
	 *            Sem pontuacao
	 * @return Correntista - Um objeto do tipo Correntista ou null caso ocorra
	 *         algum erro
	 * @exception IOException
	 *                - Trata excecoes do tipo IO
	 */
	public Correntista recuperarDadosDoCliente(String cpf) {

		InputStream is = System.in;
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader stdin = new BufferedReader(isr);
		Correntista correntista = null;

		try {
			int count = 0;
			String nome = "";
			String senhaCadastrada = "";
			double saldo = 0;

			// Recupera dados do cliente
			FileReader fr = new FileReader(cpf);
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				switch (count) {
				case (0):
					nome = br.readLine();
					break;
				case (1):
					senhaCadastrada = br.readLine();
					break;
				case (2):
					saldo = Double.parseDouble(br.readLine());
					break;
				}
				count++;
			}
			br.close();
			fr.close();

			// Efetua validacao
			System.out
					.print("Parece que voce ja possue um cadastro conosco, entao, digite sua senha e pressione <Enter>: ");
			String senha = stdin.readLine();
			if (senha.equals(senhaCadastrada)) {
				correntista = new Correntista(cpf, nome, senha, saldo);
				System.out.println("Validacao efetuada com sucesso!");
			} else {
				for (int i = 3; i >= 1; i--) {
					// Trocadilho entre tentativas e tentativa
					if (i == 1)
						System.out
								.println("Senha invalida. Voce possui apenas "
										+ i + " tentativa");
					else
						System.out.println("Senha invalida. Voce ainda possui "
								+ i + " tentativas");

					System.out
							.print("Digite novamente sua senha e aperte <Enter>:");
					senha = stdin.readLine();
					if (senha.equals(senhaCadastrada)) {
						correntista = new Correntista(cpf, nome, senha, saldo);
						System.out.println("Validacao efetuada com sucesso!");
						break;
					}
				}
			}
		} catch (IOException e) {
			System.out
					.println("Erro ao recuperar dados do cliente. Por favor contate o desenvolvedor");
		}

		if (correntista == null)
			System.out.println("Erro ao recuperar cadastro do cliente");
		return correntista;

	}
/**
 * Metodo responsavel receber e salvar os dados do cliente em um arquivo
 * 
 * @param cpf Sem pontuacao
 * @return Correntista - retorna um objeto do tipo correntista, caso ocorra erros retorna null
 * @exception Exception - trata excecoes do tipo exception, isso inclui IOException
 */
	public Correntista cadastrarNovoCliente(String cpf) {
		InputStream is = System.in;
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader stdin = new BufferedReader(isr);
		Correntista correntista = null;
		try {
			System.out
					.print("Estamos vendo que voce e novo por aqui, para abrir uma nova conta digite seu nome e pressione <Enter>: ");
			String nome = stdin.readLine();
			System.out
					.print("Muito bem, para completar seu cadastro digite uma senha e pressione <Enter>: ");
			String senha = stdin.readLine();
			correntista = new Correntista(cpf, nome, senha);

			// Cria um arquivo com os dados da conta, adicionando o valor de
			// 10.000
			FileWriter fw = new FileWriter(cpf);
			PrintWriter out = new PrintWriter(fw);
			out.print(nome);
			out.println();
			out.print(senha);
			out.println();
			out.print(correntista.getSaldo());
			out.close();
			fw.close();

			// Cria um arquivo com o extrato da conta, adicionando o valor
			// inicial
			ExtratoDao extratoDao = new ExtratoDao();
			extratoDao.salvar(
					cpf,
					"Deposito no valor de R$ "
							+ String.format("%.2f", correntista.getSaldo()));
		} catch (Exception e) {
			System.out
					.println("Erro ao cadastrar novo cliente. Por favor contate o desenvolvedor");
		}

		if (correntista == null)
			System.out.println("Erro ao cadastrar novo cliente");
		return correntista;

	}

	/**
	 * Metodo responsavel por atualizar o saldo do cliente no seu respectivo arquivo
	 * 
	 * @param correntista
	 * @throws IOException
	 */
	public void atualizarSaldoCliente(Correntista correntista){
		try {
			FileWriter fw;
			fw = new FileWriter(correntista.getCpf());

			PrintWriter out = new PrintWriter(fw);
			out.print(correntista.getNome());
			out.println();
			out.print(correntista.getSenha());
			out.println();
			out.print(correntista.getSaldo());
			out.close();
			fw.close();
		} catch (IOException e) {
			System.out.println("Erro ao atualizar saldo do cliente");
		}
	}
}
