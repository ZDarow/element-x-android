package io.element.android.features.bot.impl

import dev.zacsweers.metro.Inject
import io.element.android.features.bot.api.BotService
import io.element.android.libraries.matrix.api.MatrixClient
import io.element.android.libraries.matrix.api.core.RoomId
import io.element.android.libraries.matrix.api.roomlist.RoomList
import io.element.android.libraries.matrix.api.timeline.MatrixTimelineItem
import io.element.android.libraries.matrix.api.timeline.Timeline
import io.element.android.libraries.matrix.api.timeline.item.event.MessageContent
import io.element.android.libraries.matrix.api.timeline.item.event.TextMessageType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@Inject
class MessageObserver(
    private val botService: BotService,
    private val matrixClient: MatrixClient,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val processedEventIds = mutableSetOf<String>()

    fun start() {
        Timber.d("Bot MessageObserver started")
        observeRoomLoadingState()
    }

    private fun observeRoomLoadingState() {
        matrixClient.roomListService.allRooms.loadingState
            .filter { it is RoomList.LoadingState.Loaded }
            .onEach { observeSummaries() }
            .launchIn(scope)
    }

    private fun observeSummaries() {
        matrixClient.roomListService.allRooms.summaries
            .onEach { roomList ->
                roomList.forEach { summary ->
                    val roomId = summary.roomId
                    scope.launch {
                        val joinedRoom = matrixClient.getJoinedRoom(roomId)
                        if (joinedRoom != null) {
                            observeRoomTimeline(roomId, joinedRoom.liveTimeline)
                        }
                    }
                }
            }
            .launchIn(scope)
    }

    private fun observeRoomTimeline(roomId: RoomId, timeline: Timeline) {
        timeline.timelineItems
            .onEach { items ->
                processNewItems(roomId, items, timeline)
            }
            .launchIn(scope)
    }

    private fun processNewItems(roomId: RoomId, items: List<MatrixTimelineItem>, timeline: Timeline) {
        val event = items.lastOrNull()
            ?.let { it as? MatrixTimelineItem.Event }
            ?.event
            ?: return

        if (event.isOwn) return
        val eventId = event.eventId?.value ?: return
        if (eventId in processedEventIds) return
        processedEventIds.add(eventId)

        val content = event.content as? MessageContent ?: return
        val messageType = content.type
        val body = when (messageType) {
            is TextMessageType -> messageType.body
            else -> return
        }

        if (!body.trim().startsWith("/")) return

        Timber.d("Bot: command from ${event.sender} in $roomId: $body")

        scope.launch {
            val response = botService.processMessage(
                roomId = roomId.value,
                senderId = event.sender.value,
                message = body,
            )

            if (response != null) {
                Timber.d("Bot: sending response in room $roomId")
                timeline.sendMessage(
                    body = response,
                    htmlBody = null,
                    intentionalMentions = emptyList(),
                )
            }
        }
    }
}
