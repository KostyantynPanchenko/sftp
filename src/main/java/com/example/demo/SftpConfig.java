package com.example.demo;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.filters.SftpSimplePatternFileListFilter;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizer;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizingMessageSource;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.jcraft.jsch.ChannelSftp.LsEntry;

@Configuration
public class SftpConfig {

    private final SftpParams sftpParams;

    @Autowired
    public SftpConfig(final SftpParams sftpParams) {
        this.sftpParams = sftpParams;
    }

    @Bean
    public String check() {
        System.out.println(sftpParams.getUser());
        System.out.println(sftpParams.getPassword());
        System.out.println(sftpParams.getLocalDirectoryDownload());
        System.out.println(sftpParams.getRemoteDirectoryDownload());
        System.out.println(sftpParams.getRemoteDirectoryDownloadFilter());
        return null;
    }

    @Bean
    public SessionFactory<LsEntry> sftpSessionFactory() {
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);
        factory.setHost(sftpParams.getHost());
        factory.setPort(sftpParams.getPort());
        factory.setUser(sftpParams.getUser());
        factory.setPassword(sftpParams.getPassword());
        factory.setAllowUnknownKeys(true);
        return new CachingSessionFactory<LsEntry>(factory);
    }

    @Bean
    public SftpInboundFileSynchronizer sftpInboundFileSynchronizer() {
        SftpInboundFileSynchronizer fileSynchronizer = new SftpInboundFileSynchronizer(sftpSessionFactory());
        fileSynchronizer.setDeleteRemoteFiles(false);
        fileSynchronizer.setRemoteDirectory(sftpParams.getRemoteDirectoryDownload());
        fileSynchronizer.setFilter(new SftpSimplePatternFileListFilter(sftpParams.getRemoteDirectoryDownloadFilter()));
        return fileSynchronizer;
    }

    @Bean
    @InboundChannelAdapter(channel = "fromSftpChannel", poller = @Poller(cron = "0/5 * * * * *"))
    public MessageSource<File> sftpMessageSource() {
        SftpInboundFileSynchronizingMessageSource source = new SftpInboundFileSynchronizingMessageSource(
                sftpInboundFileSynchronizer());
        source.setLocalDirectory(new File(sftpParams.getLocalDirectoryDownload()));
        source.setAutoCreateLocalDirectory(true);
        source.setLocalFilter(new AcceptOnceFileListFilter<File>());
        return source;
    }

    @Bean
    @ServiceActivator(inputChannel = "fromSftpChannel")
    public MessageHandler resultFileHandler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                System.err.println(message.getPayload());
            }
        };
    }
}
