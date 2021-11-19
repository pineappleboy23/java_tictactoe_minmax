package tic_tac_toe_minmax;

import java.util.Scanner;

public class GameLoop {

    String is_winner_or_tie(Game game_in) {
        Game.Square eval = game_in.evaluate_position(game_in.board);
        if (eval == Game.Square.T) {
            return "tie no winner";
        } else if (eval == Game.Square.X) {
            return "X's won!";
        } else if (eval == Game.Square.O) {
            return "O's won!";
        }
        return "no winner yet keep trying (jk lol it's impossible)";

    }

    public static void main() {
        Game game = new Game();

        Scanner get_it = new Scanner(System.in);




        boolean running = true;
        while(running) {


            //computer move
            game.comp_move(Game.Square.O);

            //player move
            boolean has_moved = false;

            while (!has_moved) {
                game.draw_game();
                System.out.println("Enter move in the form '02' ");

                String move = get_it.nextLine();
                if (game.check_and_do_player_move(move, Game.Square.X)) {
                    has_moved = !has_moved;
                    System.out.println("move success");
                }

            }




        }


    }
}
