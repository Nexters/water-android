package appvian.water.buddy.utilities

import appvian.water.buddy.R

object CategoryMapper {
    fun getCategoryName(idx : Int) : String{
        when(idx){
            0 -> {
                return "물"
            }
            1 -> {
                return "커피"
            }
            2 -> {
                return "차"
            }
            3 -> {
                return "우유"
            }
            4 -> {
                return "탄산음료"
            }
            5 -> {
                return "주스"
            }
            6 -> {
                return "주류"
            }
            7 -> {
                return "이온음료"
            }
            8 -> {
                return "기타"
            }
        }
        return "기타"
    }
    fun getCategoryNum(name : String) : Int{
        when(name){
            "물" -> {
                return 0
            }
            "커피" -> {
                return 1
            }
            "차" -> {
                return 2
            }
            "우유" -> {
                return 3
            }
            "탄산음료" -> {
                return 4
            }
            "주스" -> {
                return 5
            }
            "주류" -> {
                return 6
            }
            "이온음료" -> {
                return 7
            }
            "기타" -> {
                return 8
            }
        }
        return 8
    }
}