package com.server.simple.domain;

import com.server.simple.request.PostEdit;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void change(String title, String content){
        this.title = title;
        this.content = content;
    }

    public PostEditor.PostEditorBuilder toEditor(){
        return PostEditor.builder()
                .title(title)
                .content(content);
    }


    public void edit(PostEditor postEditor) {
        title = postEditor.getTitle();
        content = postEditor.getContent();
    }
}
