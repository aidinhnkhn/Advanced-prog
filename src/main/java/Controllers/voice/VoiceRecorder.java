package Controllers.voice;

import elements.chat.pm.Pm;
import elements.chat.pm.PmType;
import site.edu.Main;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class VoiceRecorder extends VoiceUtil{

    public static void captureAudio(String chatId) {
        try {
            final AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            final TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            Runnable runner = new Runnable() {
                int bufferSize = (int)format.getSampleRate() * format.getFrameSize();
                byte buffer[] = new byte[bufferSize];

                public void run() {
                    out = new ByteArrayOutputStream();
                    isRecording = true;
                    try {
                        while (isRecording) {
                            int count = line.read(buffer, 0, buffer.length);
                            if (count > 0) {
                                out.write(buffer, 0, count);
                            }
                        }
                    } finally {
                        try {
                            out.flush();
                            out.close();
                            line.close();
                            line.flush();

                            Pm pm = new Pm(PmType.Audio, Main.mainClient.getUser().getUsername());
                            pm.setContent(Base64.getEncoder().encodeToString(out.toByteArray()));
                            Main.mainClient.getServerController().sendPm(pm,chatId);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            Thread captureThread = new Thread(runner);
            captureThread.start();
        } catch (LineUnavailableException e) {
            System.out.println("Line unavailable: " );
            e.printStackTrace();
        }
    }
}
