package com.edu.manager;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.edu.orm.IEntity;

/**
 * 玩家战斗单元数据测试
 * 
 * @author root
 *
 */
@Entity
@NamedQueries({ @NamedQuery(name = PlayerFighter.IDS, query = "select p.id from PlayerFighter p") })
public class PlayerFighter implements IEntity<String> {

	public static final String IDS = "playerfighter_ids";
	@Id
	private String id;
	/** 内容 */
	@Lob
	@Column(name = "content", nullable = true)
	private byte[] content;

	static PlayerFighter valueOf(String id) {
		PlayerFighter entity = new PlayerFighter();
		entity.id = id;
		return entity;
	}

	@Override
	public String getId() {
		return id;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public void setId(String id) {
		this.id = id;
	}

}
