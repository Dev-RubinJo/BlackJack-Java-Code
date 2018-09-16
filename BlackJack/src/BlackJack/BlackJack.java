package BlackJack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

public class BlackJack {

	public static void main(String[] args) throws Exception {
		PlayBlackJack pbj = new PlayBlackJack();
		pbj.gameStart();
	}
}

class PlayBlackJack {
	Scanner key = new Scanner(System.in);
	private Player player = new Player();
	private Player computer = new Player();

	void gameStart() throws IOException, InterruptedException{
		FileReader fr = new FileReader("SaveFile.txt");
		BufferedReader br = new BufferedReader(fr);
		String playerMoney = br.readLine();
		System.out.println(playerMoney);
		if(playerMoney == null){
			playGame();
		} else if(playerMoney != null){
			//플레이어 돈과 컴퓨터 돈을 파일에 저장한 것을 읽어와 새로 지정해준다.
			StringTokenizer stk = new StringTokenizer(playerMoney);
			String sToken = "";
			for(int i = 0; i < 4; i++){
				sToken = stk.nextToken(" ");
			}
			player.settingMoney(Integer.parseInt(sToken));
			for(int i = 0; i < 4; i++){
				sToken = stk.nextToken(" ");
			}
			computer.settingMoney(Integer.parseInt(sToken));
			//플레이어 돈과 컴퓨터 돈을 출력해준다.
			System.out.println(player.getMoney() + " " + computer.getMoney());
			if(player.getMoney() == 0 || computer.getMoney() == 0) {
				//각 금액이 0원이면 게임을 끝내고 초기화한다.
				System.out.println("모든 게임을 종료합니다.");
				player.settingMoney(100);
				computer.settingMoney(100);
				playGame();
			} else {
				playGame();
			}
		}
		
		FileWriter fw = new FileWriter("SaveFile.txt");
		fw.write("Player Money : " + player.getMoney() + " Computer Money : " + computer.getMoney());
		fw.close();
		fr.close();
		br.close();
	}

	void playGame() throws InterruptedException {
		int playGame;
		System.out.println("1. 게임시작 2. 그만하기");
		playGame = key.nextInt();
		if (playGame == 1) {
			play();
		} else if (playGame == 2) {
			System.out.println("게임이 종료되었습니다.");
			printEnd();
		} else {
			System.out.println("잘못 입력하셨습니다.");
			playGame();
		}

	}
	void printEnd() throws InterruptedException{		
		System.out.println("곧 게임을 종료합니다.");
		Thread.sleep(1000);
		System.out.println("Off");
	}

	void play() throws InterruptedException {

		int betting = 10;
		player.setCardList();
		player.setCard(player.getCardList());

		System.out.print("player : ");
		player.showPresent();
		while (player.choice() != 2) {
			System.out.print("player : ");
			player.showPresent();
			if (player.getScore() < 21);
			else if (player.getScore() > 21) {
				break;
			}
		}

		computer.setCardList();
		computer.setCard(computer.getCardList());

		System.out.print("computer : ");
		computer.showPresent();
		while (computer.computerChoice() != 2) {
			System.out.print("computer : ");
			computer.showPresent();
			if (computer.getScore() < 21);
			else if (computer.getScore() > 21) {
				break;
			}
		}

		if (player.getScore() > 21 && computer.getScore() < 21) {
			System.out.println("Player Lose.");
		} else if (computer.getScore() > 21 && player.getScore() < 21) {
			System.out.println("Computer Lose.");
		} else if(player.getScore() <= 21 && computer.getScore() <= 21){
			if (player.getScore() > computer.getScore()) {
				System.out.println("Player Win");
				player.setMoney(-betting);
				computer.setMoney(betting);
			} else if (player.getScore() < computer.getScore()) {
				System.out.println("Computer Win");
				player.setMoney(betting);
				computer.setMoney(-betting);
			} else if (player.getScore() == computer.getScore()) {
				if (player.getCardCount() < computer.getCardCount()) {
					System.out.println("Player Win");
					player.setMoney(-betting);
					computer.setMoney(betting);
				} else if (player.getCardCount() > computer.getCardCount()) {
					System.out.println("Computer Win");
					player.setMoney(betting);
					computer.setMoney(-betting);
				} else {
					System.out.println("Same point. Nobody Win.");
				}
			}
		} else if(player.getScore() > 21 && computer.getScore() > 21){
			System.out.println("All Failed. Both Lose.");
		}
		clearList();
		playGame();
	}
	void clearList(){
		player.getCard().clear();
		player.getCardList().clear();
		computer.getCard().clear();
		computer.getCardList().clear();
	}
}

class Player {
	private ArrayList<Integer> cardList = new ArrayList<Integer>();
	private ArrayList<Character> card = new ArrayList<Character>();
	Scanner key = new Scanner(System.in);
	Random rd = new Random(System.currentTimeMillis());
	private int check = (int) System.currentTimeMillis() % 3;
	private int score;
	private int money = 100;

	int getScore() {
		return score;
	}

	int getCardCount() {
		return card.size();
	}

	ArrayList<Integer> getCardList() {
		return cardList;
	}

	ArrayList<Character> getCard() {
		return card;
	}

	void settingMoney(int m) {
		this.money = m;
	}

	void setMoney(int betting) {
		this.money = money - betting;
	}

	int getMoney() {
		return this.money;
	}

	void setCard(ArrayList<Integer> cardList) {
		for (int i = 0; i < cardList.size(); i++) {
			if (cardList.get(i) == 11) {
				card.add('A');
			} else if (cardList.get(i) == 10 && check == 0) {
				card.add('K');
			} else if (cardList.get(i) == 10 && check == 1) {
				card.add('Q');
			} else if (cardList.get(i) == 10 && check == 2) {
				card.add('J');
			} else if (cardList.get(i) > 0 && cardList.get(i) < 10) {
				card.add(String.valueOf(cardList.get(i)).charAt(0));
			} else {
				return;
			}
		}
	}

	void setCardList() {
		for (int i = 0; i < 2; i++)
			cardList.add(rd.nextInt(11) + 1);
	}

	int choice() {// 플레이어가 카드를 더 선택할지 말지 결정하는 함수.
		int choice;
		int returnValue = 0;
		System.out.println("1. Hit  2. Stay");
		choice = key.nextInt();
		switch (choice) {
		case 1:
			returnValue = 1;
			System.out.println("카드를 뽑습니다.");
			cardList.add(rd.nextInt(11) + 1);
			if (cardList.get(cardList.size() - 1) == 11) {
				card.add('A');
			} else if (cardList.get(cardList.size() - 1) == 10 && check == 0) {
				card.add('K');
			} else if (cardList.get(cardList.size() - 1) == 10 && check == 1) {
				card.add('Q');
			} else if (cardList.get(cardList.size() - 1) == 10 && check == 2) {
				card.add('J');
			} else if (cardList.get(cardList.size() - 1) > 0 && cardList.get(cardList.size() - 1) < 10) {
				card.add(String.valueOf(cardList.get(cardList.size() - 1)).charAt(0));
			}
			break;
		case 2:
			returnValue = 2;
			System.out.println("턴을 넘깁니다.");
			break;
		}
		return returnValue;
	}

	int computerChoice() throws InterruptedException {// 컴퓨터가 카드를 더 선택할지 말지 결정하는 함수.
		int choice;
		int returnValue = 0;
		System.out.println("1. Hit  2. Stay");
		choice = rd.nextInt(2) + 1;
		switch (choice) {
		case 1:
			returnValue = 1;
			System.out.println("Thinking…");
			cardList.add(rd.nextInt(11) + 1);
			if (cardList.get(cardList.size() - 1) == 11) {
				card.add('A');
			} else if (cardList.get(cardList.size() - 1) == 10 && check == 0) {
				card.add('K');
			} else if (cardList.get(cardList.size() - 1) == 10 && check == 1) {
				card.add('Q');
			} else if (cardList.get(cardList.size() - 1) == 10 && check == 2) {
				card.add('J');
			} else if (cardList.get(cardList.size() - 1) > 0 && cardList.get(cardList.size() - 1) < 10) {
				card.add(String.valueOf(cardList.get(cardList.size() - 1)).charAt(0));
			}
			for(int i = 0; i < rd.nextInt(5) + 1; i++){
				System.out.print(". ");
				Thread.sleep((int)rd.nextInt(7) * 500);
			}
			System.out.println("");
			break;
		case 2:
			returnValue = 2;
			System.out.println("Thinking….");
			for(int i = 0; i < rd.nextInt(5) + 1; i++){
				System.out.print(". ");
				Thread.sleep((int)rd.nextInt(8) * 500);
			}
			System.out.println("");
			break;
		}
		return returnValue;
	}

	void showPresent() {// 현재상황을 나타내주는 함수 현재의 카드와 스코어를 보여준다.
		score = 0;

		System.out.print("현재 카드 : ");
		for (int i = 0; i < card.size(); i++) {
			System.out.print(card.get(i) + " ");
		}
		System.out.println();
		for (int i = 0; i < cardList.size(); i++)
			score += cardList.get(i);
		System.out.println("현재 스코어 : " + score);
	}
}