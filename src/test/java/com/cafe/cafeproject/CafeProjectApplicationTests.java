package com.cafe.cafeproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CafeProjectApplicationTests {

    @Test
    public void test(){
        String [] ddd = {"SOO","OOO","OOO"};
        String [] sss = {"E 2","S 2","W 1"};
//
//        String [] ddd = {"SOO","OXX","OOO"};
//        String [] sss = {"E 2","S 2","W 1"};

//        String [] ddd = {"OSO","OOO","OXO","OOO"};
//        String [] sss = {"E 2","S 3","W 1"};

        int [] arr = this.solution(ddd, sss);
    }

    public int[] solution(String[] park, String[] routes) {
        int[] answer = {};

        String [] start = new String[2];
        String [][] arr = new String[park[0].split("").length][park.length]; // 배열 추출

        //시작 좌표부터 찾아야함
        for(int i = 0; i < park.length; i++){
            String [] parkArr = park[i].split("");
            for(int j = 0; j < parkArr.length; j++){
                if(parkArr[j].equalsIgnoreCase("S")){
                    start[0] = String.valueOf(i);
                    start[1] = String.valueOf(j);
                }

                arr[i][j] = parkArr[j];
            }
        }

        System.out.println("배열 스타트 : " + start[0] + ","+ start[1]);

        int i1 = Integer.parseInt(start[0]);
        int j1 = Integer.parseInt(start[1]);

        for (int route = 0 ; route < routes.length; route ++ ){
            String [] routeArr = routes[route].split(" ");

            String rot = routeArr[0]; // N, E, S, W
            int su = Integer.parseInt(routeArr[1]);  // 움직이는 숫자

            if(rot.equalsIgnoreCase("N")|| rot.equalsIgnoreCase("W")){ //북/서
                System.out.println("0 - su" + (0 - su));
                su = (0 - su);
            }



            if(rot.equalsIgnoreCase("N") || rot.equalsIgnoreCase("S")){

                if(rot.equalsIgnoreCase("N")){ // N

                    if(i1 + su < 0)  continue;//
                    else {
                        for(int i = i1; i > (-(i1 + su)) +1; i--){
                            if(arr[i][j1].equalsIgnoreCase("X")) break;
                            else{
                                i1 = i;
                            }
                        }
                    }
                }else{
                    if(i1 + su >  arr.length)  continue;//
                    else {
                        for(int i = i1; i < (i1 + su) -1; i++){
                            if(arr[i][j1].equalsIgnoreCase("X")) break;
                            else{
                                i1 = i;
                            }
                        }
                    }
                }
            }else{
                if(rot.equalsIgnoreCase("W")){ // W
                    if(j1 + su < 0)  continue;//
                    else {
                        for(int i = j1; i < (-(j1 + su)) + 1; i--){
                            if(arr[i1][i].equalsIgnoreCase("X")) break;
                            else{
                                j1 = i;
                            }
                        }
                    }
                }else{ // E
                    if(j1 + su > arr.length)  continue;//
                    else {
                        for(int i = j1; i < (j1 + su) -1; i++){
                            if(arr[i1][i].equalsIgnoreCase("X")) break;
                            else{
                                j1 = i;
                            }
                        }
                    }
                }
            }
        }

        System.out.println("최종 : " + i1 + "," + j1);


        return answer;
    }
}
