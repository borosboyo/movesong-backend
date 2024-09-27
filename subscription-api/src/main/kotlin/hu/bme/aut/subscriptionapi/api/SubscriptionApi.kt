package hu.bme.aut.subscriptionapi.api

import hu.bme.aut.subscriptionapi.dto.req.CancelSubscriptionReq
import hu.bme.aut.subscriptionapi.dto.req.FindSubscriptionReq
import hu.bme.aut.subscriptionapi.dto.req.SubscriptionReq
import hu.bme.aut.subscriptionapi.dto.resp.CancelSubscriptionResp
import hu.bme.aut.subscriptionapi.dto.resp.FindSubscriptionResp
import hu.bme.aut.subscriptionapi.dto.resp.SubscriptionResp
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "subscription")
@Api(tags = ["subscription"], value = "Subscription controller to handle all subscription related requests")
interface SubscriptionApi {
    @PostMapping(value = ["/subscription"])
    @ApiOperation(value = "subscription ", response = SubscriptionResp::class, nickname = "subscription")
    fun subscription(@RequestBody req: SubscriptionReq): ResponseEntity<SubscriptionResp>

    @PostMapping(value = ["/cancelSubscription"])
    @ApiOperation(value = "cancelSubscription ", response = CancelSubscriptionResp::class, nickname = "cancelSubscription")
    fun cancelSubscription(@RequestBody req: CancelSubscriptionReq): ResponseEntity<CancelSubscriptionResp>

    @PostMapping(value = ["/findSubscription"])
    @ApiOperation(value = "findSubscription ", response = FindSubscriptionResp::class, nickname = "findSubscription")
    fun findSubscription(@RequestBody req: FindSubscriptionReq): ResponseEntity<FindSubscriptionResp>
}