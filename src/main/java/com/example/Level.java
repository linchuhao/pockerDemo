package com.example;

public enum Level {
  HIGH_CARD("High card", 1),
  PAIR("Pair", 2),
  TWO_PAIRS("Two Pairs", 3),
  THREE_OF_KIND("Three of kind", 4),
  STRAIGHT("Straight", 5),
  FLUSH("Flush", 6),
  FULL_HOUSE("Full House", 7),
  FOUR_OF_A_KIND("Four of a kind", 8),
  STRAIGHT_FLUSH("Straight flush", 9);

  private String kind;
  private int level;

  Level(String kind, int level) {
    this.kind = kind;
    this.level = level;
  }

  public String getKind() {
    return kind;
  }

  public void setKind(String kind) {
    this.kind = kind;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }
}
