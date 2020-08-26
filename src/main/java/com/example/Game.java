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
        if (pokerList.get(i).getValue() == 14 && pokerList.get(i - 1).getValue() == 5) {
          isStraight = true;
          transformAce(pokerList, i);
        }
      }
    }
    if (isFlush && isStraight) {
      return STRAIGHT_FLUSH;
    }
    if (isFourOfAKind(0, pokerList) || isFourOfAKind(1, pokerList)) {
      return FOUR_OF_A_KIND;
    }
    if (isFullHouse(pokerList)) {
      return FULL_HOUSE;
    }

    return HIGH_CARD;
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
    int four;
    int three;
    List<Integer> pairList=new ArrayList<Integer>();
    List<Integer> singleList=new ArrayList<Integer>();
    for(Map.Entry<Integer, Integer> entry: blackPokerMap.entrySet())
    {
//      System.out.println("Key: "+ entry.getKey()+ " Value: "+entry.getValue());
      if (entry.getValue() == 4) {
        four = entry.getKey();
      }
      if (entry.getValue() == 3) {
        three = entry.getKey();
      }
      if (entry.getValue() == 2) {
        pairList.add(entry.getKey());
      }
      if (entry.getValue() == 1) {
        singleList.add(entry.getKey());
      }
    }

//    List<Map.Entry<Integer, Integer>> blackList = new ArrayList<>(blackPokerMap.entrySet());
//    List<Map.Entry<Integer, Integer>> whiteList = new ArrayList<>(whitePokerMap.entrySet());
//    blackList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
//    whiteList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
//    for (int i = 0; i < blackList.size(); i++) {
//      if (blackList.get(i).getKey() < whiteList.get(i).getKey()) {
//        return "White win. - with high card " + whiteList.get(i).getKey();
//      }
//      if (blackList.get(i).getKey() > whiteList.get(i).getKey()) {
//        return "Black win. - with high card " + blackList.get(i).getKey();
//      }
//    }
    return "Tie";
  }

  private void transformAce(List<Poker> pokerList, int index) {
    pokerList.get(index).setValue(1);
    pokerList.sort(Comparator.comparing(Poker::getValue));
  }
}
