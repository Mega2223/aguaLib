package net.Mega2223.utils.objects;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static net.Mega2223.utils.imageTools.createFlipped;

public class graphRenderer {

    public List<List<double[]>> values;
    public Dimension dimension;
    public Color[] colours; //britanico pra não dar ambiguidade, genial dmite
    public graphRenderer(List<List<double[]>> val, Dimension dims, Color[] colors) {
        values = val;
        dimension = dims;
        colours = colors;
    }

    public static BufferedImage getImage(List<List<double[]>> dobros, Dimension dim, Color[] colors) {
        BufferedImage ret = new BufferedImage(dim.height, dim.width, BufferedImage.TYPE_4BYTE_ABGR);

        List<double[]> gr = dobros.get(0);

        double minX = gr.get(0)[0];
        double maxX = gr.get(0)[0];
        double minY = gr.get(0)[1];
        double maxY = gr.get(0)[1];

        for (List<double[]> act : dobros) {
            for (double e[] : act) {
                if (e[0] > maxX) {
                    maxX = e[0];
                }
                if (e[1] > maxY) {
                    maxY = e[1];
                }
                if (e[0] < minX) {
                    minX = e[0];
                }
                if (e[1] < minY) {
                    minY = e[1];
                }
            }
        }

        double multIndexY = (dim.getHeight() / (maxY - minY));
        double multIndexX = (dim.getWidth() / (maxX - minX));

        multIndexY = multIndexY - (multIndexY / 100);
        multIndexX = multIndexX - (multIndexX / 100);

        List<List<double[]>> translated = translateGr(dobros);

        Graphics graphics = ret.getGraphics();

        double loc0X = (-minX) * multIndexX;

        double loc0Y = (-minY) * multIndexY;


        graphics.setColor(Color.gray);
        graphics.drawLine((int) loc0X, 0, (int) loc0X, ret.getHeight());
        graphics.drawLine(0, (int) loc0Y, ret.getWidth(), (int) loc0Y);

        //x1 y1 x2 y2

        //-3,3 (L100)
        //0,6
        //3 pra frente

        //-3,3
        //3
        //


        double[] ant = null;
        int ind = 0;
        for (List<double[]> act : translated) {
            graphics.setColor(colors[ind]);
            for (double[] inte : act) {
                if (ant == null) {
                    ant = inte;
                    continue;
                }
                int TXA = (int) (ant[0] * multIndexX);
                int TYA = (int) (ant[1] * multIndexY);
                int ACX = (int) (inte[0] * multIndexX);
                int ACY = (int) (inte[1] * multIndexY);
                graphics.drawLine(TXA, TYA, ACX, ACY);
                //System.out.println( "(" + (int)ant[0] + "\\"+ (int)ant[1] +"\\"+ (int)inte[0] + "\\"+ (int)inte[1] +"):   " + TXA + "\\" + TYA + "\\" + ACX + "\\" +ACY);
                ant = inte;
            }
            graphics.setColor(Color.red);
            ind++;
            ant = null;
        }

        graphics.dispose();


        //System.out.println("==================================");
        return createFlipped(ret);
    }

    protected static List<List<double[]>> translateGr(List<List<double[]>> gr) {
        List<List<double[]>> ret = new ArrayList();

        List<double[]> first = gr.get(0);
        double minX = first.get(0)[0];
        double maxX = first.get(0)[0];
        double minY = first.get(0)[1];
        double maxY = first.get(0)[1];

        for (List<double[]> act : gr) {
            for (double e[] : act) {
                if (e[0] > maxX) {
                    maxX = e[0];
                }
                if (e[1] > maxY) {
                    maxY = e[1];
                }
                if (e[0] < minX) {
                    minX = e[0];
                }
                if (e[1] < minY) {
                    minY = e[1];
                }
            }
        }
        //System.out.println(maxX +","+minX +","+maxY +","+minY +",");

        for (List<double[]> act : gr) {
            List<double[]> fin = new ArrayList<>();
            for (double e[] : act) {
                fin.add(new double[]{e[0] - minX, e[1] - minY});
            }
            ret.add(fin);
        }

        return ret;
    }

    //todo coloca isso em outra classe

    public BufferedImage renderImage() {
        return getImage(values, dimension, colours);

    }
}