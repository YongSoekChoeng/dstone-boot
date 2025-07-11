package net.dstone.common.mq.rabbitmq.utils;

import net.dstone.common.core.BaseObject;
import net.dstone.common.utils.FileUtil;

public class RabbitMqUtil extends BaseObject{
	
	public static void main(String[] args) {
		
		String conts = makeDefualtConfig();
	    System.out.println(conts);
		String filePath = "C:/Temp";
		String fileName = "rabbitmq.json";
		FileUtil.writeFile( filePath, fileName, conts  );
		
	}
	
	/**
	 * RabbitMQ 기본설정파일 생성 메소드.
	 * @return
	 */
	public static String makeDefualtConfig() {
		StringBuffer buff = new StringBuffer();
		
		/* Virtual Host */
		String vHost = "/dstone-mq";

		buff.append("{").append("\n");
		buff.append("    \"rabbit_version\": \"3.13.0\",").append("\n");
		buff.append("    \"rabbitmq_version\": \"3.13.0\",").append("\n");
		buff.append("    \"product_name\": \"RabbitMQ\",").append("\n");
		buff.append("    \"product_version\": \"3.13.7\",").append("\n");
		buff.append("    \"users\": [").append("\n");
		buff.append("        {").append("\n");
		buff.append("            \"name\": \"guest\",").append("\n");
		buff.append("            \"password_hash\": \"dbniyabyH5mxPHGJyJpK2e7R4XyANERH/Lss27YMru0X4n93\",").append("\n"); // 패스워드: guest
		buff.append("            \"hashing_algorithm\": \"rabbit_password_hashing_sha256\",").append("\n");
		buff.append("            \"tags\": [").append("\n");
		buff.append("                \"administrator\"").append("\n");
		buff.append("            ]").append("\n");
		buff.append("        },").append("\n");
		buff.append("        {").append("\n");
		buff.append("            \"name\": \"jysn007\",").append("\n");
		buff.append("            \"password_hash\": \"s87PV0Z/0MiRA2Ve6MzWbLzG00In5EFLxjIa1kocaBSqHr55\",").append("\n"); // 패스워드: db2admin
		buff.append("            \"hashing_algorithm\": \"rabbit_password_hashing_sha256\",").append("\n");
		buff.append("            \"tags\": []").append("\n");
		buff.append("        }").append("\n");
		buff.append("    ],").append("\n");
		buff.append("    \"vhosts\": [").append("\n");
		buff.append("        {").append("\n");
		buff.append("            \"name\": \"/\",").append("\n");
		buff.append("            \"description\": \"\",").append("\n");
		buff.append("            \"tags\": [],").append("\n");
		buff.append("            \"default_queue_type\": \"classic\",").append("\n");
		buff.append("            \"metadata\": {").append("\n");
		buff.append("                \"description\": \"\",").append("\n");
		buff.append("                \"tags\": [],").append("\n");
		buff.append("                \"default_queue_type\": \"classic\"").append("\n");
		buff.append("            }").append("\n");
		buff.append("        },").append("\n");
		buff.append("        {").append("\n");
		buff.append("            \"name\": \"" + vHost + "\",").append("\n");
		buff.append("            \"description\": \"\",").append("\n");
		buff.append("            \"tags\": [],").append("\n");
		buff.append("            \"default_queue_type\": \"classic\",").append("\n");
		buff.append("            \"metadata\": {").append("\n");
		buff.append("                \"description\": \"\",").append("\n");
		buff.append("                \"tags\": [],").append("\n");
		buff.append("                \"default_queue_type\": \"classic\"").append("\n");
		buff.append("            }").append("\n");
		buff.append("        }").append("\n");
		buff.append("    ],").append("\n");
		buff.append("    \"permissions\": [").append("\n");
		buff.append("        {").append("\n");
		buff.append("            \"user\": \"guest\",").append("\n");
		buff.append("            \"vhost\": \"/\",").append("\n");
		buff.append("            \"configure\": \".*\",").append("\n");
		buff.append("            \"write\": \".*\",").append("\n");
		buff.append("            \"read\": \".*\"").append("\n");
		buff.append("        },").append("\n");
		buff.append("        {").append("\n");
		buff.append("            \"user\": \"jysn007\",").append("\n");
		buff.append("            \"vhost\": \"" + vHost + "\",").append("\n");
		buff.append("            \"configure\": \".*\",").append("\n");
		buff.append("            \"write\": \".*\",").append("\n");
		buff.append("            \"read\": \".*\"").append("\n");
		buff.append("        }").append("\n");
		buff.append("    ],").append("\n");
		buff.append("    \"parameters\": [],").append("\n");
		buff.append("    \"global_parameters\": [],").append("\n");
		buff.append("    \"policies\": [],").append("\n");
		
		/* Queue */
		buff.append("    \"queues\": [").append("\n");
		// fanout.exchange 용 공지사항등...
		buff.append("        {").append("\n");
		buff.append("            \"name\": \"app.notifications.queue\",").append("\n");
		buff.append("            \"vhost\": \"" + vHost + "\",").append("\n");
		buff.append("            \"durable\": true,").append("\n");
		buff.append("            \"auto_delete\": false,").append("\n");
		buff.append("            \"arguments\": {").append("\n");
		buff.append("                \"x-dead-letter-exchange\": \"dlx.default.exchange\"").append("\n");
		buff.append("            }").append("\n");
		buff.append("        },").append("\n");
		// direct.exchange 용
		buff.append("        {").append("\n");
		buff.append("            \"name\": \"app.orders.queue\",").append("\n");
		buff.append("            \"vhost\": \"" + vHost + "\",").append("\n");
		buff.append("            \"durable\": true,").append("\n");
		buff.append("            \"auto_delete\": false,").append("\n");
		buff.append("            \"arguments\": {").append("\n");
		buff.append("                \"x-dead-letter-exchange\": \"dlx.default.exchange\"").append("\n");
		buff.append("            }").append("\n");
		buff.append("        },").append("\n");
		// dlx.default.exchange 용
		buff.append("        {").append("\n");
		buff.append("            \"name\": \"dlx.failed.messages.queue\",").append("\n");
		buff.append("            \"vhost\": \"" + vHost + "\",").append("\n");
		buff.append("            \"durable\": true,").append("\n");
		buff.append("            \"auto_delete\": false,").append("\n");
		buff.append("            \"arguments\": {}").append("\n");
		buff.append("        }").append("\n");
		buff.append("    ],").append("\n");
		
		/* Exchanges */
		buff.append("    \"exchanges\": [").append("\n");
		// Fanout Exchange: 모든 바인딩된 큐로 메시지를 브로드캐스트 (알림, 로그 전파 등에 유용).
		buff.append("        {").append("\n");
		buff.append("            \"name\": \"app.fanout.exchange\",").append("\n");
		buff.append("            \"vhost\": \"" + vHost + "\",").append("\n");
		buff.append("            \"type\": \"fanout\",").append("\n");
		buff.append("            \"durable\": true,").append("\n");
		buff.append("            \"auto_delete\": false,").append("\n");
		buff.append("            \"arguments\": {}").append("\n");
		buff.append("        },").append("\n");
		// Direct Exchange: 특정 서비스로 메시지를 라우팅 (점대점 통신에 유용).
		buff.append("        {").append("\n");
		buff.append("            \"name\": \"app.direct.exchange\",").append("\n");
		buff.append("            \"vhost\": \"" + vHost + "\",").append("\n");
		buff.append("            \"type\": \"direct\",").append("\n");
		buff.append("            \"durable\": true,").append("\n");
		buff.append("            \"auto_delete\": false,").append("\n");
		buff.append("            \"arguments\": {}").append("\n");
		buff.append("        },").append("\n");
		// Dead Letter Exchange (DLX) 설정: 처리되지 못한(NACK, reject, TTL 만료 등) 메시지를 별도의 큐로 전달하여 메시지 손실을 방지하고 디버깅을 도움.
		buff.append("        {").append("\n");
		buff.append("            \"name\": \"dlx.default.exchange\",").append("\n");
		buff.append("            \"vhost\": \"" + vHost + "\",").append("\n");
		buff.append("            \"type\": \"topic\",").append("\n");
		buff.append("            \"durable\": true,").append("\n");
		buff.append("            \"auto_delete\": false,").append("\n");
		buff.append("            \"arguments\": {}").append("\n");
		buff.append("        }").append("\n");
		buff.append("    ],").append("\n");
		buff.append("    \"bindings\": [").append("\n");
		buff.append("        {").append("\n");
		buff.append("            \"source\": \"app.fanout.exchange\",").append("\n");
		buff.append("            \"vhost\": \"" + vHost + "\",").append("\n");
		buff.append("            \"destination\": \"app.notifications.queue\",").append("\n");
		buff.append("            \"destination_type\": \"queue\",").append("\n");
		buff.append("            \"routing_key\": \"\",").append("\n");
		buff.append("            \"arguments\": {}").append("\n");
		buff.append("        },").append("\n");
		buff.append("        {").append("\n");
		buff.append("            \"source\": \"app.direct.exchange\",").append("\n");
		buff.append("            \"vhost\": \"" + vHost + "\",").append("\n");
		buff.append("            \"destination\": \"app.orders.queue\",").append("\n");
		buff.append("            \"destination_type\": \"queue\",").append("\n");
		buff.append("            \"routing_key\": \"orders.process\",").append("\n");
		buff.append("            \"arguments\": {}").append("\n");
		buff.append("        },").append("\n");
		buff.append("        {").append("\n");
		buff.append("            \"source\": \"dlx.default.exchange\",").append("\n");
		buff.append("            \"vhost\": \"" + vHost + "\",").append("\n");
		buff.append("            \"destination\": \"dlx.failed.messages.queue\",").append("\n");
		buff.append("            \"destination_type\": \"queue\",").append("\n");
		buff.append("            \"routing_key\": \"#\",").append("\n");
		buff.append("            \"arguments\": {}").append("\n");
		buff.append("        }").append("\n");
		buff.append("    ]").append("\n");
		buff.append("}").append("\n");
		
		return buff.toString();
	}
}
