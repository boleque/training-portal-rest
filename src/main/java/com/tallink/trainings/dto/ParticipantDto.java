package com.tallink.trainings.dto;

import com.tallink.trainings.model.Participant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDto {
    private Long id;

    private String name;

    public static ParticipantDto toDto(Participant participant) {
        return new ParticipantDto(
                participant.getId(),
                participant.getName()
        );
    }

    public Participant toModel() {
        Participant participant = new Participant();
        participant.setId(getId());
        participant.setName(getName());
        return participant;
    }
}
