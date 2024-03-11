package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.*;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String type;
    private final String nric;
    private final String name;
    private final String dob;
    private final String phone;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("type") String type, @JsonProperty("nric") String nric,
            @JsonProperty("name") String name, @JsonProperty("dob") String dob,
            @JsonProperty("phone") String phone) {
        this.type = type;
        this.nric = nric;
        this.name = name;
        this.dob = dob;
        this.phone = phone;
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        type = source.getType().toString();
        nric = source.getNRIC().nric;
        name = source.getName().fullName;
        dob = source.getDoB().dateOfBirth.toString();
        phone = source.getPhone().value;
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        if (nric == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, NRIC.class.getSimpleName()));
        }
        if (!NRIC.isValidNRIC(nric)) {
            throw new IllegalValueException(NRIC.MESSAGE_CONSTRAINTS);
        }
        final NRIC modelNRIC = new NRIC(nric);

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (dob == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, DoB.class.getSimpleName()));
        }
        if (!DoB.isValidDoB(dob)) {
            throw new IllegalValueException(DoB.MESSAGE_CONSTRAINTS);
        }
        final DoB modelDoB = new DoB(dob);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        switch (type) {
            case "PATIENT":
                return new Patient(modelNRIC, modelName, modelDoB, modelPhone);
        }

        return null;
    }

}
