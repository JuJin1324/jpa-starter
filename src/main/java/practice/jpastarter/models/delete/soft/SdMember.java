package practice.jpastarter.models.delete.soft;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2021/11/06
 */

@Entity
@Table(name = "SD_MEMBER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SdMember extends SoftDeleteEntity {
    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "UUID", unique = true)
    private String uuid;

    @Column(name = "NAME")
    private String name;

    @Column(name = "AGE")
    private Integer age;

    @Column(name = "PHONE", unique = true)
    private String phone;

    public SdMember(String uuid, String name, Integer age, String phone) {
        this.uuid = uuid;
        this.name = name;
        this.age = age;
        this.phone = phone;
    }

    public void update(String name, Integer age, String phone) {
        this.name = name;
        this.age = age;
        this.phone = phone;
    }
}
