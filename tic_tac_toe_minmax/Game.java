package tic_tac_toe_minmax;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Game {
    enum Square {
        O,
        X,
        E,
        T,
    }



    Square[][] board = {
            {Square.E, Square.E, Square.E},
            {Square.E, Square.E, Square.E},
            {Square.E, Square.E, Square.E}
    };

    class MinMaxReturn {
        Square return_square;
        String move_i_should_do;
        MinMaxReturn(Square return_square, String move_i_should_do) {
            this.return_square = return_square;
            this.move_i_should_do = move_i_should_do;
        }

    }



    String[] get_open_spots(Square[][] game_in){
        List<String> available_spots = new ArrayList<String>();
        int counter1 = -1;
        int counter2 = -1;

        for (Square[] i : game_in){
            counter1 += 1;
            counter2 = -1;
            for (Square ii : i){
                counter2 += 1;
                if (ii == Square.E){
                    available_spots.add("" + counter1 + counter2);

                }
            }
        }
        return available_spots.toArray(String[]::new);
    }
    boolean check_and_do_player_move(String p_move, Square player_type){
        String[] possible_moves = this.get_open_spots(this.board);
        for (String i : possible_moves){
            if (i.equals(p_move)){
                board[Character.getNumericValue(p_move.charAt(0))][Character.getNumericValue(p_move.charAt(1))] = player_type;
                return true;
            }
        }
        return false;
    }

    Square[][] return_changed_board(String move_to_do, Square[][] game_in, Square player_type) {
        game_in[Character.getNumericValue(move_to_do.charAt(0))][Character.getNumericValue(move_to_do.charAt(1))] = player_type;
        return game_in;
    }

    void comp_move(Square c_type) {
        String[] possible_moves = this.get_open_spots(this.board);
        draw_game();
        /*  test to see if memory address is changed
        Square[][] safe_board2 = this.copy_board(this.board);

        safe_board2[0][0] = Square.E;
        System.out.println("cloned");
        this.draw_game_from_input(safe_board2);
        System.out.println("clone^");
        this.draw_game();
        */
        MinMaxReturn jeff = this.min_max( this.copy_board(this.board), c_type) ;
        String c_move = jeff.move_i_should_do ;


        draw_game();
        board[Character.getNumericValue(c_move.charAt(0))][Character.getNumericValue(c_move.charAt(1))] = c_type;


    }

    MinMaxReturn min_max(final Square[][] game_state, Square player) {
        Square eval = evaluate_position(game_state);
        this.draw_game_from_input(game_state);
        if ( eval != Square.E ) {
            String str1 = new String("this should never be a move");
            return new MinMaxReturn(eval, str1);
        } else {
            boolean tie_possible = false;
            String tie_move = "this should never return";
            String[] open_spots = this.get_open_spots(game_state);
            List<Square> scores = new ArrayList<>();
            for (String i : open_spots) {
                //it was the problem I fixed it tho
                // I think the line below is the problem I think it is changing game state, but it should only read it
                Square child_eval = this.min_max( this.return_changed_board(i, this.copy_board(game_state).clone(), player), this.switch_type(player) ).return_square;
                scores.add(child_eval);
                if (child_eval == player ) {
                   return new MinMaxReturn(player, i);
                } else if (child_eval == Square.T ) {
                    tie_possible = true;
                    tie_move = i;
                }
            }
            if (tie_possible) {
                return new MinMaxReturn(Square.T, tie_move);
            } else {
                return new MinMaxReturn(this.switch_type(player), open_spots[0]);
            }
        }
    }

    /*MinMaxReturn min_max(Square[][] game_state, Square player) {
        String[] pos_moves = this.get_open_spots(game_state);
        List<Square> moves_score = new ArrayList<>();



        while (true) {
            if ( pos_moves.length == moves_score.size()) {
                System.out.println("if 1 top");
                draw_game();
                Square best_position = moves_score.get(0);
                int counter = -1;
                int pos_of_best_pos = counter;
                for (Square i : moves_score) {
                    counter += 1;
                    if (i == player) {
                        return new MinMaxReturn(player, pos_moves[counter]);
                    }
                    if (i == Square.T){
                        best_position = i;
                        pos_of_best_pos = counter;
                    }
                }
                System.out.println("if 1 bot");
                return new MinMaxReturn(best_position, pos_moves[pos_of_best_pos]);

            } else if ( this.evaluate_position( this.return_changed_board(pos_moves[moves_score.size()], game_state, player) ) == Square.E) {
                System.out.println("if 2 top");
                draw_game();
                if (player == Square.X) {
                    moves_score.add( min_max(this.return_changed_board(pos_moves[moves_score.size()], game_state, player), Square.O).return_square );
                } else {
                    moves_score.add( min_max(this.return_changed_board(pos_moves[moves_score.size()], game_state, player), Square.X).return_square );
                }
                System.out.println("if 2 bot");
            } else {
                System.out.println("if 3 top");
                draw_game();
                moves_score.add( this.evaluate_position( this.return_changed_board(pos_moves[moves_score.size()], game_state, player)));
                System.out.println("if 3 bot");
            }
        }

    } */

    Square switch_type(Square type_in) {
        if (type_in == Square.X){
            return Square.O;
        }
        return Square.X;
    }

    Square evaluate_position(Square[][] position_in) {
        int[][][] winning_position = {
                {{0,0}, {1,0}, {2,0}},
                {{0,1}, {1,1}, {2,1}},
                {{0,2}, {1,2}, {2,2}},

                {{0,0}, {0,1}, {0,2}},
                {{1,0}, {1,1}, {1,2}},
                {{2,0}, {2,1}, {2,2}},

                {{0,0}, {1,1}, {2,2}},
                {{0,2}, {1,1}, {2,0}}
        };
        int ctr = -1;
        for (int[][] i : winning_position){
            ctr += 1;
            if ( position_in[i[0][0]][i[0][1]] != Square.E & ((position_in[i[0][0]][i[0][1]] == position_in[i[1][0]][i[1][1]]) & (position_in[i[0][0]][i[0][1]] == position_in[i[2][0]][i[2][1]]))) {
                return position_in[i[0][0]][i[0][1]];
            }
        }
        for (Square[] i : position_in) {
            for (Square ii : i) {
                if (ii == Square.E) {
                    return Square.E;
                }
            }
        }
            return Square.T;

    }

    Square[][] copy_board(Square[][] board_in) {
        Square[][] new_board = {
                {Square.E, Square.E, Square.E},
                {Square.E, Square.E, Square.E},
                {Square.E, Square.E, Square.E}
        };
        int[] count_2_three = {0,1,2};
        for (int i : count_2_three) {
            for (int ii : count_2_three) {
                new_board[i][ii] = board_in[i][ii];
            }
        }
        return new_board;
    }

    void draw_game() {
        System.out.println("    X0  X1  X2");
        System.out.println("0X  " + board[0][0] + " | " + board[0][1] + " | " + board[0][2]);
        System.out.println("   ___________");
        System.out.println("1X  " + board[1][0] + " | " + board[1][1] + " | " + board[1][2]);
        System.out.println("   ___________");
        System.out.println("2X  " + board[2][0] + " | " + board[2][1] + " | " + board[2][2]);
    }



    void draw_game_from_input(Square[][] board_in) {
        System.out.println("    X0  X1  X2");
        System.out.println("0X  " + board_in[0][0] + " | " + board_in[0][1] + " | " + board_in[0][2]);
        System.out.println("   ___________");
        System.out.println("1X  " + board_in[1][0] + " | " + board_in[1][1] + " | " + board_in[1][2]);
        System.out.println("   ___________");
        System.out.println("2X  " + board_in[2][0] + " | " + board_in[2][1] + " | " + board_in[2][2]);
    }

}
