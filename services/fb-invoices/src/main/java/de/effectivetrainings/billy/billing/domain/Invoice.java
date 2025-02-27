/**
 Copyright (c) 2013, Martin Dilger - Effective Trainings & Consulting - EffectiveTrainings.de
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in the
 documentation and/or other materials provided with the distribution.
 * Neither the name of Effective Trainings & Consulting nor the
 names of its contributors may be used to endorse or promote products
 derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL EffectiveTrainings BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package de.effectivetrainings.billy.billing.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

/**
 * @author <a href=mailto:martin@effectivetrainings.de">Martin Dilger</a>
 * @since: 28.03.14
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Invoice implements Amountable {

    @JsonProperty(value = "invoiceNumber")
    private String invoiceNumber;

    @JsonProperty(value = "amount")
    private Amount amountValue;

    @JsonProperty(value = "organization")
    private String organization;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty(value = "date")
    private Date invoiceDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty(value = "paidDate")
    private Date paidDate;

    @JsonProperty(value = "cancelled")
    private boolean cancelled;

    @JsonProperty(value = "paid")
    public boolean isPaid() {
        return paidDate != null;
    }


    public Optional<LocalDateTime> getPaidMonth() {
        Date paidDate = getPaidDate();
        if(paidDate == null) {
            return null;
        }

        LocalDateTime localDate = toLocalDateTime(paidDate);
        return Optional.of(localDate);
    }

    public boolean paidIn(Integer month, Integer year) {
        if(month == null && year == null) {
            //no filter supplied
            return true;
        }
        if(!isPaid()) {
            return false;
        }

        LocalDateTime paidDate = toLocalDateTime(getPaidDate());
        int paidMonth = paidDate.getMonthValue();
        int paidYear = paidDate.getYear();
        return (month != null ? paidMonth == month : true) &&
            (year != null ? paidYear == year : true);
    }

    private LocalDateTime toLocalDateTime(Date date) {
        if(date == null)
            return null;
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
    }
}
