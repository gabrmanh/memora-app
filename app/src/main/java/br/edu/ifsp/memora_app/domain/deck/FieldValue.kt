package br.edu.ifsp.memora_app.domain.deck

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "field_values",
    primaryKeys = ["cardId", "fieldId"], // composite PK
    foreignKeys = [
        ForeignKey(
            entity = Card::class,
            parentColumns = ["id"],
            childColumns = ["cardId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Field::class,
            parentColumns = ["id"],
            childColumns = ["fieldId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FieldValue(
    val cardId: String,
    val fieldId: String,
    val value: String
)