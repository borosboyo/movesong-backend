package hu.bme.aut.subscriptionapi.api

import hu.bme.aut.subscriptionapi.dto.req.*
import hu.bme.aut.subscriptionapi.dto.resp.CancelSubscriptionResp
import hu.bme.aut.subscriptionapi.dto.resp.FindSubscriptionResp
import hu.bme.aut.subscriptionapi.dto.resp.SaveSubscriptionResp
import hu.bme.aut.subscriptionapi.dto.resp.SubscriptionResp
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "subscription", url = "http://subscription:8083/subscription")
@Api(tags = ["subscription"], value = "Subscription controller to handle all subscription related requests")
interface SubscriptionApi {
    @PostMapping(value = ["/subscription"])
    @ApiOperation(value = "subscription ", response = SubscriptionResp::class, nickname = "subscription")
    fun subscription(@RequestBody req: SubscriptionReq): ResponseEntity<SubscriptionResp>

    @PostMapping(value = ["/cancelSubscription"])
    @ApiOperation(value = "cancelSubscription ", response = CancelSubscriptionResp::class, nickname = "cancelSubscription")
    fun cancelSubscription(@RequestBody req: CancelSubscriptionReq): ResponseEntity<CancelSubscriptionResp>

    @PostMapping(value = ["/saveSubscription"])
    @ApiOperation(value = "saveSubscription ", response = SaveSubscriptionResp::class, nickname = "saveSubscription")
    fun saveSubscription(@RequestBody req: SaveSubscriptionReq): ResponseEntity<SaveSubscriptionResp>

    @PostMapping(value = ["/findSubscription"])
    @ApiOperation(value = "findSubscription ", response = FindSubscriptionResp::class, nickname = "findSubscription")
    fun findSubscription(@RequestBody req: FindSubscriptionReq): ResponseEntity<FindSubscriptionResp>

    @PostMapping(value = ["/findSubscriptionByUserEmail"])
    @ApiOperation(value = "findSubscriptionByUserEmail ", response = FindSubscriptionResp::class, nickname = "findSubscriptionByUserEmail")
    fun findSubscriptionByUserEmail(@RequestBody req: FindSubscriptionByUserEmailReq): ResponseEntity<FindSubscriptionResp?>
}