package lotto.domain;

import lotto.Lotto;
import lotto.LottoTicket;

import java.util.ArrayList;
import java.util.List;

public class LottoController {
    private static Lotto lotto;
    private LottoTicketGenerator lottoTicketGenerator;
    private UserInput userInput;
    private UserInputChecker userInputChecker;

    public LottoController() {
        lottoTicketGenerator = new LottoTicketGenerator();
        userInput = new UserInput();
        userInputChecker = new UserInputChecker();
    }

    public enum LottoMessage {
        PURCHASE_AMOUNT_MESSAGE("구입금액을 입력해 주세요."),
        PURCHASE_COUNT_MESSAGE("개를 구매했습니다."),
        REQUEST_WINNING_NUMBERS("당첨 번호를 입력해 주세요."),
        REQUEST_BOUNS_NUMBERS("보너스 번호를 입력해 주세요."),
        WINNING_STATS_HEADER("당첨 통계 ln ---");

        private String message;

        LottoMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public void playGame() {
        List<LottoTicket> userLottoTickets = new ArrayList<>();
        int purchaseAmount = handlePurchaseAmount();

        userLottoTickets = lottoTicketGenerator.generateLottoTicket(purchaseAmount);
        printTickets(userLottoTickets); // 구매한 로또들 번호 출력

        lotto = new Lotto(handleWinningNumbers()); // 당첨번호 입력, lotto 객체에 저장
        handleCompareWinningNumbers(userLottoTickets); // 당첨번호와 추첨번호 비교

    }

    public void handleCompareWinningNumbers(List<LottoTicket> userLottoTickets) {
        int matchCount = 0;
        for (LottoTicket lottoTicket : userLottoTickets) {
            matchCount = lotto.compareWinningNumbers(lottoTicket);
            lottoTicket.setMatchCount(matchCount);
        }
    }

    public List<Integer> handleWinningNumbers() {
        List<Integer> winningNumbers = new ArrayList<>();
        do {
            System.out.println(LottoMessage.REQUEST_WINNING_NUMBERS.getMessage()); // 당첨번호 입력 안내문구 출력
            String input = userInput.input();
            winningNumbers = userInput.getUserWinningNumbers(userInputChecker, input);
        } while (winningNumbers == null);

        return winningNumbers;
    }

    public int handlePurchaseAmount() {
        int purchaseAmount = 0;
        do {
            System.out.println(LottoMessage.PURCHASE_AMOUNT_MESSAGE.getMessage()); // 구입금액 입력 안내문구 출력
            int ticketCount = 0;
            String input = userInput.input();
            purchaseAmount = userInput.getUserPurchaseAmount(userInputChecker, input);
            ticketCount = purchaseAmount / 1000;
            if (ticketCount != 0)
                System.out.println(ticketCount + LottoMessage.PURCHASE_COUNT_MESSAGE.getMessage()); // 구매한 로또 개수 출력
        } while (purchaseAmount == 0);
        return purchaseAmount;
    }

    public void printTickets(List<LottoTicket> userLottoTickets) {
        for (LottoTicket ticket : userLottoTickets) {
            System.out.println(ticket.getNumbers());
        }
    }
}
