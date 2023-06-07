/*
 * Copyright 2023 The Android Open Source Project
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

package androidx.appactions.interaction.capabilities.productivity

import androidx.appactions.builtintypes.types.Alarm
import androidx.appactions.builtintypes.types.GenericErrorStatus
import androidx.appactions.builtintypes.types.SuccessStatus
import androidx.appactions.interaction.capabilities.core.BaseExecutionSession
import androidx.appactions.interaction.capabilities.core.Capability
import androidx.appactions.interaction.capabilities.core.CapabilityFactory
import androidx.appactions.interaction.capabilities.core.impl.converters.EntityConverter
import androidx.appactions.interaction.capabilities.core.impl.converters.TypeConverters
import androidx.appactions.interaction.capabilities.core.impl.spec.ActionSpecBuilder
import androidx.appactions.interaction.capabilities.core.properties.Property
import androidx.appactions.interaction.capabilities.serializers.types.ALARM_TYPE_SPEC
import androidx.appactions.interaction.proto.ParamValue
import androidx.appactions.interaction.protobuf.Struct
import androidx.appactions.interaction.protobuf.Value

private const val CAPABILITY_NAME = "actions.intent.DISMISS_ALARM"

/** A capability corresponding to actions.intent.DISMISS_ALARM */
@CapabilityFactory(name = CAPABILITY_NAME)
class DismissAlarm private constructor() {
    internal enum class SlotMetadata(val path: String) {
        ALARM("alarm")
    }

    class CapabilityBuilder : Capability.Builder<
        CapabilityBuilder,
        Arguments,
        Output,
        Confirmation,
        ExecutionSession
        >(ACTION_SPEC) {
        fun setAlarmProperty(alarm: Property<Alarm>): CapabilityBuilder = setProperty(
            SlotMetadata.ALARM.path,
            alarm,
            EntityConverter.of(ALARM_TYPE_SPEC)
        )
    }

    class Arguments internal constructor(val alarm: AlarmValue?) {
        override fun toString(): String {
            return "Arguments(alarm=$alarm)"
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Arguments

            if (alarm != other.alarm) return false

            return true
        }

        override fun hashCode(): Int {
            return alarm.hashCode()
        }

        class Builder {
            private var alarm: AlarmValue? = null

            fun setAlarm(alarm: AlarmValue): Builder = apply { this.alarm = alarm }

            fun build(): Arguments = Arguments(alarm)
        }
    }
    class Output internal constructor(val executionStatus: ExecutionStatus?) {
        override fun toString(): String {
            return "Output(executionStatus=$executionStatus)"
        }

        override fun hashCode(): Int {
            return executionStatus.hashCode()
        }

        class Builder {
            private var executionStatus: ExecutionStatus? = null

            fun setExecutionStatus(executionStatus: ExecutionStatus): Builder = apply {
                this.executionStatus = executionStatus
            }

            fun setExecutionStatus(successStatus: SuccessStatus) = apply {
                this.setExecutionStatus(ExecutionStatus(successStatus))
            }

            fun setExecutionStatus(genericErrorStatus: GenericErrorStatus) = apply {
                this.setExecutionStatus(ExecutionStatus(genericErrorStatus))
            }

            fun build(): Output = Output(executionStatus)
        }
    }

    class ExecutionStatus {
        private var successStatus: SuccessStatus? = null
        private var genericErrorStatus: GenericErrorStatus? = null

        constructor(successStatus: SuccessStatus) {
            this.successStatus = successStatus
        }

        constructor(genericErrorStatus: GenericErrorStatus) {
            this.genericErrorStatus = genericErrorStatus
        }

        internal fun toParamValue(): ParamValue {
            var status: String = ""
            if (successStatus != null) {
                status = successStatus.toString()
            }
            if (genericErrorStatus != null) {
                status = genericErrorStatus.toString()
            }
            val value: Value = Value.newBuilder().setStringValue(status).build()
            return ParamValue.newBuilder()
                .setStructValue(
                    Struct.newBuilder().putFields(TypeConverters.FIELD_NAME_TYPE, value).build()
                )
                .build()
        }
    }

    sealed interface ExecutionSession : BaseExecutionSession<Arguments, Output>
    class Confirmation internal constructor()

    companion object {
        private val ACTION_SPEC =
            ActionSpecBuilder.ofCapabilityNamed(CAPABILITY_NAME)
                .setArguments(Arguments::class.java, Arguments::Builder, Arguments.Builder::build)
                .setOutput(Output::class.java)
                .bindParameter(
                    SlotMetadata.ALARM.path,
                    Arguments.Builder::setAlarm,
                    AlarmValue.PARAM_VALUE_CONVERTER
                )
                .bindOutput(
                    "executionStatus",
                    Output::executionStatus,
                    ExecutionStatus::toParamValue
                )
                .build()
    }
}