package com.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.example.Level.HIGH_CARD;
import static com.example.Level.STRAIGHT_FLUSH;

public class Game {

  private int calculateValue(char value) {
    if (value == 'A') {
      return 14;
    }
    if (value == 'T') {
      return 10;
    }
    if (value == 'J') {
      return 11;
    }
    if (value == 'Q') {
      return 12;
    }
    if (value == 'K') {
      return 13;
    }
    return value - 48;
  }

  private PokerHands generatePokerHands(String cards) {
    String[] pokers = cards.split(" ");
    PokerHands pokerHands = new PokerHands();
    List<Poker> pokerList = new ArrayList<>();
    pokerHands.setPokers(pokerList);
    for (String pokerStr : pokers) {
      Poker poker = new Poker();
      poker.setValue(calculateValue(pokerStr.charAt(0)));
      poker.setKind(String.valueOf(pokerStr.charAt(1)));
      pokerList.add(poker);
    }
    pokerList.sort(Comparator.comparing(Poker::getValue));
    return pokerHands;
  }

  private Level level(PokerHands pokerHands) {
    List<Poker> pokerList = pokerHands.getPokers();
    boolean isFlush = true;
    boolean isStraight = true;
    for (int i = 1; i < pokerList.size(); i++) {
      if (!pokerList.get(0).getKind().equals(pokerList.get(i).getKind())) {
        isFlush = false;
      }
      if (pokerList.get(i).getValue() - 1 != pokerList.get(i - 1).getValue()) {
        isStraight = false;
        if (pokerList.get(i).getValue() == 14 && pokerList.get(i-1).getValue() == 5) {
          isStraight = true;
          transformAce(pokerList, i);
        }
      }
    }

    if (isFlush && isStraight) {
      return STRAIGHT_FLUSH;
    }

    return HIGH_CARD;
  }

  public String run(String black, String white) {
    PokerHands blackPokerHands = generatePokerHands(black);
    PokerHands whitePokerHands = generatePokerHands(white);
    Level blackLevel = level(blackPokerHands);
    Level whiteLevel = level(whitePokerHands);
    if (blackLevel.getLevel() > whiteLevel.getLevel()) {
      return "Black win. - with " + blackLevel.getKind();
    }
    if (blackLevel.getLevel() == whiteLevel.getLevel()) {
      List<Poker> blackPokerList = blackPokerHands.getPokers();
      List<Poker> whitePokerList = whitePokerHands.getPokers();
      if (blackPokerList.get(blackPokerList.size() - 1).getValue()
          > whitePokerList.get(blackPokerList.size() - 1).getValue()) {
        return "Black win. - with high card "
            + blackPokerList.get(blackPokerList.size() - 1).getValue();
      } else if (blackPokerList.get(blackPokerList.size() - 1).getValue()
          < whitePokerList.get(blackPokerList.size() - 1).getValue()){
        return "White win. - with high card "
            + whitePokerList.get(whitePokerList.size() - 1).getValue();
      } else {
        return "Tie";
      }
    }
    return null;
  }

  private void transformAce(List<Poker> pokerList, int index){
    pokerList.get(index).setValue(1);
    pokerList.sort(Comparator.comparing(Poker::getValue));
  }
}
