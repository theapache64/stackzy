/*
 * Copyright 2021 Boil (https://github.com/theapache64/boil)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.theapache64.stackzy.util.flow

import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * To fire events.
 * This flow won't fire the last value for each collect call.
 * This observer will only be invoked on `tryEmit` calls.
 * (replacement for SingleLiveEvent :D)
 * Created by theapache64 : Jan 08 Fri,2021 @ 01:40
 */
fun <T> mutableEventFlow(): MutableSharedFlow<T> {
    return MutableSharedFlow(
        replay = 0,
        extraBufferCapacity = 1
    )
}
