package hu.bme.aut.subscription.controller

import hu.bme.aut.subscription.service.SubscriptionService
import hu.bme.aut.subscriptionapi.api.SubscriptionApi
import hu.bme.aut.subscriptionapi.dto.req.*
import hu.bme.aut.subscriptionapi.dto.resp.CancelSubscriptionResp
import hu.bme.aut.subscriptionapi.dto.resp.FindSubscriptionResp
import hu.bme.aut.subscriptionapi.dto.resp.SaveSubscriptionResp
import hu.bme.aut.subscriptionapi.dto.resp.SubscriptionResp
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/subscription")
class SubscriptionController(
    private val subscriptionService: SubscriptionService
) : SubscriptionApi {

    override fun subscription(@RequestBody req: SubscriptionReq): ResponseEntity<SubscriptionResp> {
        return ResponseEntity.ok(subscriptionService.subscription(req))
    }

    override fun cancelSubscription(@RequestBody req: CancelSubscriptionReq): ResponseEntity<CancelSubscriptionResp> {
        return ResponseEntity.ok(subscriptionService.cancelSubscription(req))
    }

    override fun saveSubscription(@RequestBody req: SaveSubscriptionReq): ResponseEntity<SaveSubscriptionResp> {
        return ResponseEntity.ok(subscriptionService.saveSubscription(req))
    }

    override fun findSubscription(@RequestBody req: FindSubscriptionReq): ResponseEntity<FindSubscriptionResp> {
        return ResponseEntity.ok(subscriptionService.findSubscription(req))
    }

    override fun findSubscriptionByUserEmail(@RequestBody req: FindSubscriptionByUserEmailReq): ResponseEntity<FindSubscriptionResp?> {
        return ResponseEntity.ok(subscriptionService.findSubscriptionByUserEmail(req))
    }
}
