package com.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.Level.FOUR_OF_A_KIND;
import static com.example.Level.FULL_HOUSE;
import static com.example.Level.HIGH_CARD;
import static com.example.Level.STRAIGHT_FLUSH;
import static com.example.Level.FLUSH;
import static com.example.Level.STRAIGHT;
import static com.example.Level.THREE_OF_KIND;
import static com.example.Level.PAIR;
import static com.example.Level.TWO_PAIRS;

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

  private boolean isFourOfAKind(int startIndex, List<Poker> pokerList) {
    return pokerList.get(startIndex).getValue() == pokerList.get(startIndex + 1).getValue()
        && pokerList.get(startIndex + 1).getValue() == pokerList.get(startIndex + 2).getValue()
        && pokerList.get(startIndex + 2).getValue() == pokerList.get(startIndex + 3).getValue();
  }

  private boolean isFullHouse(List<Poker> pokerList) {
    return isThreeOfKind(0, pokerList) && isPair(3, pokerList)
        || isPair(0, pokerList) && isThreeOfKind(2, pokerList);
  }

  private boolean isThreeOfKind(int startIndex, List<Poker> pokerList) {
    return pokerList.get(startIndex).getValue() == pokerList.get(startIndex + 1).getValue()
        && pokerList.get(startIndex + 1).getValue() == pokerList.get(startIndex + 2).getValue();
  }

  private boolean isPair(int startIndex, List<Poker> pokerList) {
    return pokerList.get(startIndex).getValue() == pokerList.get(startIndex + 1).getValue();
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
      }
    }
    isStraight = isStraightWithAce(pokerList, isStraight);
    if (isFlush && isStraight) {
      return STRAIGHT_FLUSH;
    }
    if (isFourOfAKind(0, pokerList) || isFourOfAKind(1, pokerList)) {
      return FOUR_OF_A_KIND;
    }
    if (isFullHouse(pokerList)) {
      return FULL_HOUSE;
    }
    if (isFlush) {
      return FLUSH;
    }
    if (isStraight) {
      return STRAIGHT;
    }
    if (isThreeOfKind(0, pokerList) || isThreeOfKind(2, pokerList)) {
      return THREE_OF_KIND;
    }
    if (isPair(0, pokerList) && isPair(2, pokerList) || isPair(1, pokerList) && isPair(3, pokerList)) {
      return TWO_PAIRS;
    }
    if (isPair(0, pokerList) || isPair(1, pokerList) || isPair(2, pokerList) || isPair(3, pokerList)) {
      return PAIR;
    }
    return HIGH_CARD;
  }

  private boolean isStraightWithAce(List<Poker> pokerList, boolean isStraight) {
    if (pokerList.get(4).getValue() == 14 && pokerList.get(0).getValue() == 2){
      isStraight = true;
      for (int i =1; i < pokerList.size() -1; i++) {
        if (pokerList.get(i).getValue() - 1 != pokerList.get(i - 1).getValue()) {
          isStraight = false;
          break;
        }
      }
      if (isStraight) {
        transformAce(pokerList, 4);
      }
    }
    return isStraight;
  }

  public String run(String black, String white) {
    PokerHands blackPokerHands = generatePokerHands(black);
    PokerHands whitePokerHands = generatePokerHands(white);
    return getResult(blackPokerHands, whitePokerHands);
  }

  private String getResult(PokerHands blackPokerHands, PokerHands whitePokerHands) {
    Level blackLevel = level(blackPokerHands);
    Level whiteLevel = level(whitePokerHands);
    if (blackLevel.getLevel() > whiteLevel.getLevel()) {
      return "Black win. - with " + blackLevel.getKind();
    } else if (blackLevel.getLevel() < whiteLevel.getLevel()) {
      return "White win. - with " + whiteLevel.getKind();
    } else {
      return getResultWithSameLevel(blackPokerHands, whitePokerHands);
    }
  }

  private String getResultWithSameLevel(PokerHands blackPokerHands, PokerHands whitePokerHands) {
    List<Poker> blackPokerList = blackPokerHands.getPokers();
    List<Poker> whitePokerList = whitePokerHands.getPokers();
    HashMap<Integer, Integer> blackPokerMap = new HashMap<>();
    HashMap<Integer, Integer> whitePokerMap = new HashMap<>();
    for (int i = 4; i >= 0; i--) {
      if (blackPokerMap.containsKey(blackPokerList.get(i).getValue())) {
        int key = blackPokerList.get(i).getValue();
        blackPokerMap.put(key, blackPokerMap.get(key) + 1);
      } else {
        blackPokerMap.put(blackPokerList.get(i).getValue(), 1);
      }
      if (whitePokerMap.containsKey(whitePokerList.get(i).getValue())) {
        int key = whitePokerList.get(i).getValue();
        whitePokerMap.put(key, whitePokerMap.get(key) + 1);
      } else {
        whitePokerMap.put(whitePokerList.get(i).getValue(), 1);
      }
    }
    List<Map.Entry<Integer, Integer>> blackList = new ArrayList<>(blackPokerMap.entrySet());
    List<Map.Entry<Integer, Integer>> whiteList = new ArrayList<>(whitePokerMap.entrySet());
    blackList.sort(Map.Entry.comparingByValue());
    whiteList.sort(Map.Entry.comparingByValue());
    for (int i = blackList.size() - 1; i >= 0; i--) {
      if (blackList.get(i).getKey().equals(whiteList.get(i).getKey())) {
        continue;
      }
      if (blackList.get(i).getKey() < whiteList.get(i).getKey()) {
        return "White win. - with high card " + transformCard(whiteList.get(i).getKey());
      }
      if (blackList.get(i).getKey() > whiteList.get(i).getKey()) {
        return "Black win. - with high card " + transformCard(blackList.get(i).getKey());
      }
    }
    return "Tie";
  }

  private void transformAce(List<Poker> pokerList, int index) {
    pokerList.get(index).setValue(1);
    pokerList.sort(Comparator.comparing(Poker::getValue));
  }

  private String transformCard(int value) {
    if (value == 10)
      return "T";
    if (value == 11)
      return "J";
    if (value == 12)
      return "Q";
    if (value == 13)
      return "K";
    if (value == 14)
      return "A";
    return String.valueOf(value);
  }
}
