package com.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameTest {

  private static Game game;

  @BeforeAll
  public static void setUp() {
    game = new Game();
  }

  @Test
  public void
      should_be_return_black_win_when_given_pokers_Black_3H_4H_5H_6H_7H_White_3H_3D_3S_5C_3D() {
    // given
    String white = "3H 3D 3S 5C 3D";
    String black = "3H 4H 5H 6H 7H";
    // when
    String result = game.run(black, white);
    // then
    assertEquals("Black win. - with Straight flush", result);
  }
}
