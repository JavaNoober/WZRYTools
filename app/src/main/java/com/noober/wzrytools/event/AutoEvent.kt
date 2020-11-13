package com.noober.wzrytools.event

import android.graphics.Point
import com.noober.wzrytools.AssistAccessService
import com.noober.wzrytools.base.WeChatAccessibilityBaseUtil

object AutoEvent {

    private val SHOP_POSITION = Point(140, 437)

    private var EQUIPMENT_POSITION = Point(958, 928)

    private val SALE_POSITION = Point(1965, 863)

    private val DEFENSE_POSITION = Point(344, 595)

    private val ATTACK_POSITION = Point(344, 407)

    private val MOVE_POSITION_START = Point(1666, 800)

    private val MOVE_POSITION_END = Point(1666, 149)

    private val REVIVE_POSITION = Point(1400, 665)

    private val MINGDAO_MOVE_POSITION_START = Point(1666, 700)

    private val MINGDAO_MOVE_POSITION_END = Point(1666, 200)

    private val MINGDAO_REVIVE_POSITION = Point(1000, 710)

    private val BUY_POSITION = Point(1963, 941)

    private val CLOSE_POSITION = Point(2008, 100)

    public fun setEquipmentPosition(x: Int, y: Int){
        EQUIPMENT_POSITION = Point(x, y)
    }


    public fun autoChange(service: AssistAccessService?) {
        if (service == null) {
            return
        }
        WeChatAccessibilityBaseUtil.click(service, SHOP_POSITION)
        WeChatAccessibilityBaseUtil.delay(200)
        WeChatAccessibilityBaseUtil.click(service, EQUIPMENT_POSITION)
        WeChatAccessibilityBaseUtil.delay(200)
        WeChatAccessibilityBaseUtil.click(service, SALE_POSITION)
        WeChatAccessibilityBaseUtil.delay(200)
        WeChatAccessibilityBaseUtil.click(service, ATTACK_POSITION)
        WeChatAccessibilityBaseUtil.delay(200)
        WeChatAccessibilityBaseUtil.move(service, MINGDAO_MOVE_POSITION_START, MINGDAO_MOVE_POSITION_END) {
//            WeChatAccessibilityBaseUtil.delay(200)
            WeChatAccessibilityBaseUtil.click(service, MINGDAO_REVIVE_POSITION)
            WeChatAccessibilityBaseUtil.delay(200)
            WeChatAccessibilityBaseUtil.click(service, BUY_POSITION)
            WeChatAccessibilityBaseUtil.delay(200)
            WeChatAccessibilityBaseUtil.click(service, CLOSE_POSITION)
        }

    }
}